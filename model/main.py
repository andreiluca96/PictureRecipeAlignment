import json
import os

import matplotlib.image
import numpy as np
from keras import Input, Model
from keras.applications.imagenet_utils import preprocess_input
from keras.layers import Dense, Lambda, K
from keras.optimizers import rmsprop
from skimage.transform import resize

from model import ResNet152

ANNOTATED_FILE = './data/det_ingrs_truncated.json'
MAPPING_FILE = './data/layer2_truncated.json'
PHOTOS_FOLDER = './data/photos'
IMAGE_FOLDER = 'D:/Master/IA/Data_1/imagini'
train_data = []
merged_model = 0


def load_json_file(path):
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


def to_one_hot(ingredients, mapping):
    one_hot = np.zeros(len(mapping))
    for ingredient in ingredients:
        one_hot[mapping[ingredient]] = 1

    return one_hot


def load_image(image_name):
    return matplotlib.image.imread(os.path.join(PHOTOS_FOLDER, image_name))


def preprocess(x):
    x = resize(x, (224, 224), mode='constant') * 255
    x = preprocess_input(x)
    if x.ndim == 3:
        x = np.expand_dims(x, 0)
    return x


def get_dense_model(input_shape, output_size):
    input_layer = Input(shape=input_shape, name='ingredients_input')
    first_layer = Dense(100, activation='relu')(input_layer)
    output_layer = Dense(output_size, activation='relu', name="ingredients_output")(first_layer)

    return input_layer, output_layer


def cosine_distance(vests):
    x, y = vests
    x = K.l2_normalize(x, axis=-1)
    y = K.l2_normalize(y, axis=-1)
    return -K.mean(x * y, axis=-1, keepdims=True)


def cos_dist_output_shape(shapes):
    shape1, shape2 = shapes
    return shape1[0], 1


def predict_image(img):
    img = preprocess(img)
    all_ingredients = list(map(lambda entry: entry['ingredients'], train_data))
    img_repeated = [img for _ in range(len(all_ingredients))]
    predictions = merged_model.predict([all_ingredients, img_repeated])
    predictions = np.array(predictions)
    return train_data[np.argmin(predictions)[0]]


def predict_ingredients(ingrs):
    all_ingredients = list(map(lambda entry: entry['ingredients'], train_data))
    ingrs = list(filter(lambda x: x in all_ingredients, ingrs))

    all_images = list(map(lambda entry: entry['image'], train_data))
    ingrs_repeated = [ingrs for _ in range(len(all_images))]
    predictions = merged_model.predict([ingrs_repeated, all_images])
    predictions = np.array(predictions)
    return train_data[np.argmin(predictions)[0]]


def setup_train_data():
    train_tags = load_json_file(ANNOTATED_FILE)
    train_texts = []
    all_ingredients = set()
    for x in train_tags:
        current_ingredients_list = []
        for is_valid, tag in zip(x['valid'], x['ingredients']):
            if is_valid:
                real_text = tag['text']
                current_ingredients_list.append(real_text)
                all_ingredients.add(real_text)
        train_texts.append({'ingredients': current_ingredients_list, 'id': x['id']})

    ingredients_one_hot_position = {}
    for idx, ingr in enumerate(all_ingredients):
        ingredients_one_hot_position[ingr] = idx

    ingredients_photo_mappings_json = load_json_file(MAPPING_FILE)
    ingredients_photo_mappings = []
    for mapping in ingredients_photo_mappings_json:
        ingredients_photo_mappings.append(
            {'id': mapping['id'], 'images': list(map(lambda x: x['id'], mapping['images']))})

    merged_dict = merge_lists(train_texts, ingredients_photo_mappings, 'id')
    for item in merged_dict:
        real_item = merged_dict[item]
        for image in real_item['images']:
            train_data.append({'id': real_item['id'],
                               'ingredients': to_one_hot(real_item['ingredients'], ingredients_one_hot_position),
                               'image': load_image(image)})


if __name__ == "__main__":
    setup_train_data()
    image_input, image_output = ResNet152(include_top=False, input_shape=(224, 224, 3), pooling='avg', weights=None)
    image_model = Dense(100, activation='relu', name='image_output')(image_output)
    dense_model_input, dense_model_output = get_dense_model((len(train_data),), 100)

    output = Lambda(cosine_distance, output_shape=cos_dist_output_shape)([dense_model_output, image_model])

    merged_model = Model(inputs=[dense_model_input, image_input], outputs=output)
    merged_model.compile(optimizer=rmsprop(0.0001), loss='mean_squared_error')

    merged_model.fit(
        [list(map(lambda x: x['ingredients'], train_data)), list(map(lambda x: preprocess(x['image'])[0], train_data))],
        np.zeros((len(train_data),)), epochs=10, validation_split=0.1)

    # merged_model.predict(
    #     x=[[list(map(lambda x: x['ingredients'], train_data))[2]], [list(map(lambda x: preprocess(x['image'])[0], train_data))[2]]])
    #

    test_image = load_image("test_image1.jpg")
    merged_model.predict(
        x=[list(map(lambda x: x['ingredients'], train_data)),
           [preprocess(test_image)[0] for i in range(5)]])
