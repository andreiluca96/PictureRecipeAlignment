from __future__ import print_function

# Handle data
import json
import numpy as np
import re

from gensim.models import word2vec
from keras import Sequential
from keras.callbacks import ModelCheckpoint
from keras.layers import Embedding, LSTM, Dropout, TimeDistributed, Activation, Dense
from keras.optimizers import Adam
from keras.utils import to_categorical
from keras_preprocessing.sequence import pad_sequences
from keras_preprocessing.text import Tokenizer
from nltk.corpus import stopwords
from nltk.stem import SnowballStemmer

TRAIN_DATA_FILE = './input/layer1-truncated.json'

def text_to_wordlist(text, remove_stopwords=False, stem_words=False):
    text = text.lower().split()

    if remove_stopwords:
        stops = set(stopwords.words("english"))
        text = [w for w in text if not w in stops]

    text = " ".join(text)

    text = re.sub(r"[^A-Za-z0-9^,!.\/'+-=]", " ", text)
    text = re.sub(r"what's", "what is ", text)
    text = re.sub(r"\'s", " ", text)
    text = re.sub(r"\'ve", " have ", text)
    text = re.sub(r"can't", "cannot ", text)
    text = re.sub(r"n't", " not ", text)
    text = re.sub(r"i'm", "i am ", text)
    text = re.sub(r"\'re", " are ", text)
    text = re.sub(r"\'d", " would ", text)
    text = re.sub(r"\'ll", " will ", text)
    text = re.sub(r",", " ", text)
    text = re.sub(r"\.", " ", text)
    text = re.sub(r"!", " ! ", text)
    text = re.sub(r"\/", " ", text)
    text = re.sub(r"\^", " ^ ", text)
    text = re.sub(r"\+", " + ", text)
    text = re.sub(r"\-", " - ", text)
    text = re.sub(r"\=", " = ", text)
    text = re.sub(r"'", " ", text)
    text = re.sub(r"(\d+)(k)", r"\g<1>000", text)
    text = re.sub(r":", " : ", text)
    text = re.sub(r" e g ", " eg ", text)
    text = re.sub(r" b g ", " bg ", text)
    text = re.sub(r" u s ", " american ", text)
    text = re.sub(r"\0s", "0", text)
    text = re.sub(r" 9 11 ", "911", text)
    text = re.sub(r"e - mail", "email", text)
    text = re.sub(r"j k", "jk", text)
    text = re.sub(r"\s{2,}", " ", text)

    if stem_words:
        text = text.split()
        stemmer = SnowballStemmer('english')
        stemmed_words = [stemmer.stem(word) for word in text]
        text = " ".join(stemmed_words)

    return text


def load_train_data(filename):
    trainrecipts = json.load(open(filename, 'r'))
    ingreds = []
    for recipe in trainrecipts:
        ingreds.append({'id': recipe['id'], 'ingredients_text': recipe['ingredients']})

    return ingreds


# ingredients_length_mean = np.mean([len(x.split(' ')) for x in ingredients])
# ingredients_length_stddev = np.std([len(x.split(' ')) for x in ingredients])

def load_model_from_file(filename):
    model = word2vec.Word2Vec.load(filename)
    model.init_sims(replace=True)
    return model


def train_model(words):
    num_features = 300
    min_word_count = 4
    num_workers = 20
    context = 12
    downsampling = 1e-3
    model = word2vec.Word2Vec(words, workers=num_workers,
                              size=num_features, min_count=min_word_count,
                              window=context, sample=downsampling)
    model.init_sims(replace=True)
    return model


def load_train_tags(path):
    with open(path, 'r') as input_tag_file:
        train_tags = json.load(input_tag_file)
        return train_tags


def merge_lists(l1, l2, key):
    merged = {}
    for item in l1 + l2:
        if item[key] in merged:
            merged[item[key]].update(item)
        else:
            merged[item[key]] = item
    return merged



def preprocess_train_data():
    train_data = load_train_data('input/layer1-truncated.json')
    train_tags = load_train_tags('input/det_ingrs-truncated.json')

    merged_train_data = merge_lists(train_tags, train_data, 'id')
    x = []
    y = []
    for entry in merged_train_data.values():
        x.append(list(filter(lambda x: entry['valid'][entry['ingredients_text'].index(x)], entry['ingredients_text'])))
        y.append(list(filter(lambda x: entry['valid'][entry['ingredients'].index(x)], entry['ingredients'])))
    x = [item['text'] for entry in x for item in entry]
    y = [item['text'] for entry in y for item in entry]

    return x, y
class KerasBatchGenerator(object):

    def __init__(self, data, num_steps, batch_size, vocabulary, skip_step=5):
        self.data = data
        self.num_steps = num_steps
        self.batch_size = batch_size
        self.vocabulary = vocabulary
        # this will track the progress of the batches sequentially through the
        # data set - once the data reaches the end of the data set it will reset
        # back to zero
        self.current_idx = 0
        # skip_step is the number of words which will be skipped before the next
        # batch is skimmed from the data set
        self.skip_step = skip_step

    def generate(self):
        x = np.zeros((self.batch_size, self.num_steps))
        y = np.zeros((self.batch_size, self.num_steps, self.vocabulary))
        while True:
            for i in range(self.batch_size):
                if self.current_idx + self.num_steps >= len(self.data):
                    # reset the index back to the start of the data set
                    self.current_idx = 0
                x[i, :] = self.data[self.current_idx:self.current_idx + self.num_steps]
                temp_y = self.data[self.current_idx + 1:self.current_idx + self.num_steps + 1]
                # convert all of temp_y into a one hot representation
                y[i, :, :] = to_categorical(temp_y, num_classes=self.vocabulary)
                self.current_idx += self.skip_step
            yield x, y


max_seqence_length = 250

if __name__ == "__main__":
    X, Y = preprocess_train_data()

    all_words = []
    ingredients_text_processed = []
    for ingr in X:
        words = re.findall("[a-zA-Z0-9'\-]+", ingr)
        ingredients_text_processed.append(' '.join(words))
        all_words.append([w.lower() for w in words])


    # model = train_model(all_words)
    model = load_model_from_file("w2v.model")

    embedded_words = model.wv.vocab.keys()
    all_words_representation = [model.wv[x] for x in embedded_words]
    tokenizer = Tokenizer(nb_words=len(embedded_words), lower=True, split=' ')
    tokenizer.fit_on_texts(embedded_words)

    sequences = tokenizer.texts_to_sequences(ingredients_text_processed)
    labels = tokenizer.texts_to_sequences(Y)

    for i in range(len(sequences)):
        sequences[i].extend(labels[i])
    data = pad_sequences(sequences, maxlen=max_seqence_length, padding='post')

    data_squeezed = np.asarray(data).reshape(-1)


    num_steps = 1 # 30
    batch_size = 1 #20
    train_data_generator = KerasBatchGenerator(data_squeezed, num_steps, batch_size, len(embedded_words), skip_step=num_steps)

    hidden_size = 500
    use_dropout = True
    model = Sequential()
    model.add(Embedding(len(embedded_words), hidden_size, input_length=num_steps))
    model.add(LSTM(hidden_size, return_sequences=True))
    model.add(LSTM(hidden_size, return_sequences=True))
    if use_dropout:
        model.add(Dropout(0.5))
    model.add(TimeDistributed(Dense(len(embedded_words))))
    model.add(Activation('softmax'))

    optimizer = Adam()
    model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['categorical_accuracy'])

    num_epochs = 50


    model.fit_generator(train_data_generator.generate(), len(data)//(batch_size*num_steps), num_epochs,
                        validation_data=train_data_generator.generate(),
                        validation_steps=len(data)//(batch_size*num_steps))

    # X_train, X_valid, Y_train, Y_valid = train_test_split(X, Y, test_size=0.20, random_state=36)
    #
    # # Here we train the Network.
    # batch_size = 32
    # model.fit(X_train, Y_train, batch_size=batch_size, nb_epoch=1, verbose=5)
