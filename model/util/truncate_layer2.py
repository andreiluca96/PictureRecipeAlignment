import json
import os

LAYER2_PATH = "../../../data/layer2.json"
IMAGES_DIRECTORY_PATH = "..\\data\\specialized_training_data"
NO_ITEMS = 5

if __name__ == "__main__":
    with open("truncated-layer2-specialized.json", "w") as truncated_file:
        with open(LAYER2_PATH) as layer2_file:
            layer2_data = json.load(layer2_file)
            truncated_data = []

            downloaded_images = os.listdir(IMAGES_DIRECTORY_PATH)

            ids = {}
            for image in downloaded_images:
                for layer2_item in layer2_data:
                    if image in list(map(lambda x: x['id'], layer2_item['images'])):
                        image_id = layer2_item['id']
                        if image_id not in ids:
                            ids[image_id] = list()
                        ids[image_id].append(image)

            final_json = []
            for key, value in ids.items():
                final_obj = {}
                final_obj['id'] = key

                final_obj['images'] = list()
                for image in value:
                    final_obj['images'].append(image)

                final_json.append(final_obj)
            json.dump(final_json, truncated_file, indent=4)
