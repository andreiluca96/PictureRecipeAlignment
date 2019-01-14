import json
import os
import shutil

LAYER2_FILE = "truncated-layer2-5.json"
IMAGES_DIRECTORY_PATH = "D:\\Master\\IA\\Data_1\\imagini\\"

if __name__ == "__main__":
    with open(LAYER2_FILE) as layer2_file:
        layer2_data = json.load(layer2_file)
        for item in layer2_data:
            for image in item['images']:
                shutil.copy(os.path.join(IMAGES_DIRECTORY_PATH, image['id']), os.path.join("..\\data\\training_photos\\", image['id']))
