import os
import numpy as np
from skimage.io import imread
from skimage.transform import resize
from keras.applications.imagenet_utils import decode_predictions
from keras.applications.imagenet_utils import preprocess_input
from model import ResNet152

os.environ['CUDA_VISIBLE_DEVICES'] = "-1"


def preprocess(x):
    x = resize(x, (224, 224), mode='constant') * 255
    x = preprocess_input(x)
    if x.ndim == 3:
        x = np.expand_dims(x, 0)
    return x


if __name__ == '__main__':

    print('loading ResNet-152 model...', end='')
    model = ResNet152()
    print('done!')

    print('Predicting image outputs...')

    import os

    for root, dirs, files in os.walk('./imgs'):
        for file in files:
            img_file = './imgs/' + file
            image = imread(img_file)
            x = preprocess(image)
            y = model.predict(x)
            pred_title = decode_predictions(y, top=1)[0][0][1]

            print(img_file, pred_title)

    print('Success!')
