from __future__ import print_function
import keras

# Handle data
import json
import operator
import collections
import re

# Handle table-like data
import numpy as np
import pandas as pd

# Model Algorithms
# we could use also tensor flow, there are multiple implementations of word2vec
from gensim.models import word2vec

# Modelling Helpers, see above the description
from keras import Sequential
from keras.layers import Bidirectional, LSTM, Activation, Dense
from sklearn.manifold import TSNE

# Visualisation
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import seaborn as sns

# Load the dataset
# json format labels: cuisine, id number and ingredients (list)
trainrecipts = json.load(open('./input/layer1.json', 'r'))
ingredients = []
for recipe in trainrecipts:
    for ingr in recipe[u'ingredients']:
        ingredients.append(ingr[u'text'])

# ingredients_length_mean = np.mean([len(x.split(' ')) for x in ingredients])
# ingredients_length_stddev = np.std([len(x.split(' ')) for x in ingredients])

# Set values for NN parameters
num_features = 300  # Word vector dimensionality
min_word_count = 4  # 50% of the corpus
num_workers = 20  # Number of CPUs
context = 12  # Context window size;
# let's use avg recipte size
downsampling = 1e-3  # threshold for configuring which
# higher-frequency words are randomly downsampled

all_words = []
for ingr in ingredients:
    words = re.findall("[a-zA-Z0-9'\-]+", ingr)
    all_words.append([w.lower() for w in words])

# Initialize and train the model
model = word2vec.Word2Vec(all_words, workers=num_workers, \
                          size=num_features, min_count=min_word_count, \
                          window=context, sample=downsampling)

# If you don't plan to train the model any further, calling
# init_sims will make the model much more memory-efficient.
model.init_sims(replace=True)

all_words_representation = [model.wv[x] for x in model.vocabulary.so]

modelLSTM = Sequential()
modelLSTM.add(Bidirectional(LSTM(10, return_sequences=True), input_shape=(300, )))
modelLSTM.add(Bidirectional(LSTM(10)))
modelLSTM.add(Dense(5))
modelLSTM.add(Activation('softmax'))
modelLSTM.compile(loss='categorical_crossentropy', optimizer='rmsprop')