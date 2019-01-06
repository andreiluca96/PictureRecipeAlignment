from keras.applications.vgg16 import VGG16
from keras.preprocessing.image import load_img
from keras.preprocessing.image import img_to_array
from keras.applications.vgg16 import decode_predictions
from keras.applications.vgg16 import preprocess_input
from keras.models import Model

model = VGG16()
print(model.summary())


# load an image from file
image = load_img('./data/burger-cropped.jpg', target_size=(224, 224))

# convert the image pixels to a numpy array
image = img_to_array(image)

# reshape data for the model
image = image.reshape((1, image.shape[0], image.shape[1], image.shape[2]))

# prepare the image for the VGG model
image = preprocess_input(image)

# predict the probability across all output classes
y = model.predict(image)

# convert the probabilities to class labels
label = decode_predictions(y)

# retrieve the most likely result, e.g. highest probability
label = label[0][0]

# print the classification
print('%s (%.2f%%)' % (label[1], label[2]*100))


# discard the prediction layer
layer_name = 'fc2'
model2 = Model(inputs=model.input, outputs=model.get_layer(layer_name).output)

# get weights

temp_weights = [layer.get_weights() for layer in model.layers]
for i in range(len(temp_weights) - 1):
    model2.layers[i].set_weights(temp_weights[i])

model2.summary()

model2.save("model2.h5")

# results
y = model2.predict(image)
print(y)
