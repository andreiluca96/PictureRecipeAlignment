import keras

import main
from flask import Flask, request, jsonify
from flask_cors import CORS
import base64
import numpy as np

app = Flask(__name__)
CORS(app)


def create_model():
    keras.backend.clear_session()
    model = main.setup_model()
    model.load_weights('model.h5')
    return model


def preprocess_image(img):
    return base64.b64decode(img)


def predict_image(img):
    img = main.preprocess(img)
    img_repeated = [img for _ in range(len(main.all_ingredients))]
    predictions = model.predict([main.all_ingredients, img_repeated])
    predictions = np.array(predictions)
    predicted_ingredients_one_hot = main.train_data[np.argmin(predictions)[0]]['ingredients']
    predicted_ingredients = [main.all_ingredients[x] for x in range(len(predicted_ingredients_one_hot)) if
                             predicted_ingredients_one_hot[x] == 1]
    return predicted_ingredients


def predict_ingredients(ingredients):
    known_ingredients = list(filter(lambda x: x in main.all_ingredients, ingredients))
    all_images = list(map(lambda entry: entry['image'], main.train_data))
    ingredients_repeated = [known_ingredients for _ in range(len(all_images))]
    predictions = np.array(model.predict([ingredients_repeated, all_images]))
    predicted_image = main.train_data[np.argmin(predictions)[0]]['image']
    return predicted_image


@app.route('/image', methods=['POST'])
def classify_image():
    return jsonify(ingredients=predict_image(preprocess_image(request.json['img'])))


@app.route('/ingredients', methods=['POST'])
def classify_ingredients():
    return jsonify(image=base64.b64encode(predict_ingredients(request.json['ingredients'])))


if __name__ == '__main__':
    main.setup_train_data()
    model = create_model()
    model.summary()
    app.run(debug=True)
