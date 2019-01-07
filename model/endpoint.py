import base64

import keras
import numpy as np
import skimage.io
import tensorflow
from flask import Flask, request, jsonify
from flask_cors import CORS
from tensorflow.python.client import session

import main

app = Flask(__name__)
CORS(app)
graph = 0


def create_model():
    keras.backend.clear_session()
    global graph
    graph = tensorflow.get_default_graph()
    init_op = tensorflow.global_variables_initializer()
    config = tensorflow.ConfigProto()
    config.gpu_options.allow_growth = True
    session = tensorflow.Session(graph=graph, config=config)

    if 'session' in locals() and session is not None:
        print('Close interactive session')
        session.close()
    model = main.setup_model()
    model.load_weights('model3.h5')
    model._make_predict_function()
    return model


def preprocess_image(img):
    return base64.b64decode(img)


def predict_image(img):
    with graph.as_default():
        main.setup_train_data()
        all_ingredient_combinations = list(map(lambda entry: entry['ingredients'], main.train_data))
        img = main.preprocess(skimage.io.imread(img, plugin='imageio'))
        img_repeated = [img for _ in range(len(all_ingredient_combinations))]
        predictions = model.predict([np.array(all_ingredient_combinations), np.squeeze(np.array(img_repeated))])
        predictions = np.array(predictions)
        try:
            predicted_ingredients_one_hot = main.train_data[np.argmin(predictions)][0]['ingredients']
        except:
            predicted_ingredients_one_hot = main.train_data[np.argmin(predictions)]['ingredients']
        predicted_ingredients = [list(main.all_ingredients)[x] for x in range(len(predicted_ingredients_one_hot)) if
                                 predicted_ingredients_one_hot[x] == 1]
        return predicted_ingredients


def predict_ingredients(ingredients):
    with graph.as_default():
        main.setup_train_data()
        known_ingredients = list(filter(lambda x: x in main.all_ingredients, ingredients))
        all_images = list(map(lambda entry: entry['image'], main.train_data))
        ingredients_repeated = [main.to_one_hot(known_ingredients, main.ingredients_one_hot_position) for _ in
                                range(len(all_images))]
        predictions = np.array(model.predict([np.array(ingredients_repeated), np.array([main.preprocess(imng)[0] for imng in all_images])]))
        predicted_image = main.train_data[np.argmin(predictions)]['image']
        return predicted_image


@app.route('/image', methods=['POST'])
def classify_image():
    return jsonify(ingredients=predict_image(preprocess_image(request.json['img'])))


@app.route('/ingredients', methods=['POST'])
def classify_ingredients():
    return jsonify(image=base64.b64encode(predict_ingredients(request.json['ingredients'])).decode('ascii')+"")


if __name__ == '__main__':
    main.setup_train_data()
    model = create_model()
    model.summary()
    app.run(debug=True)
