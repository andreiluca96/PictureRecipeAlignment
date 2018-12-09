from __future__ import print_function

# Handle data
import json
import re

from gensim.models import word2vec
from keras import Sequential
from keras.layers import Embedding, LSTM, Dense
from keras_preprocessing.sequence import pad_sequences
from keras_preprocessing.text import Tokenizer
from nltk.corpus import stopwords
from nltk.stem import SnowballStemmer
from sklearn.model_selection import train_test_split

TRAIN_DATA_FILE = './input/layer1.json'


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

    return (text)


def load_train_data(filename):
    trainrecipts = json.load(open(filename, 'r'))
    ingredients = []
    for recipe in trainrecipts:
        for ingr in recipe[u'ingredients']:
            ingredients.append(ingr[u'text'])

    return ingredients


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


#
#
# load_train_data(TRAIN_DATA_FILE)
#
# model = load_model_from_file("w2v.model")

def get_model():
    embed_dim = 128
    lstm_out = 200

    model = Sequential()
    model.add(Embedding(2500, embed_dim, input_length=data.shape[1], dropout=0.2))
    model.add(LSTM(lstm_out, dropout_U=0.2, dropout_W=0.2))
    model.add(Dense(2, activation='softmax'))
    model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
    print(model.summary())


if __name__ == "__main__":
    ingredients = load_train_data(TRAIN_DATA_FILE)

    all_words = []
    ingredients_text_processed = []
    for ingr in ingredients:
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

    data = pad_sequences(sequences, maxlen=10)

    with open('input/det_ingrs.json', 'r') as input_tag_file:
        train_tags = json.load(input_tag_file)

    model = get_model()

    X_train, X_valid, Y_train, Y_valid = train_test_split(X, Y, test_size=0.20, random_state=36)

    # Here we train the Network.
    batch_size = 32
    model.fit(X_train, Y_train, batch_size=batch_size, nb_epoch=1, verbose=5)
