import json
import os

LAYER2_PATH = "../../../data/layer2.json"
IMAGES_DIRECTORY_PATH = "../../../Data_1/imagini"
NO_ITEMS = 5

if __name__ == "__main__":
    with open("truncated-layer2-" + str(NO_ITEMS) + ".json", "w") as truncated_file:
        with open(LAYER2_PATH) as layer2_file:
            layer2_data = json.load(layer2_file)
            truncated_data = []

            downloaded_images = os.listdir(IMAGES_DIRECTORY_PATH)
            for layer2_item in layer2_data:
                current_created_item = {}
                if len(truncated_data) == NO_ITEMS:
                    break
                current_created_item['id'] = layer2_item['id']
                current_created_images_list = []
                for image in layer2_item['images']:
                    if image['id'] in downloaded_images:
                        current_created_images_list.append(image)
                if len(current_created_images_list) > 0:
                    current_created_item['images'] = current_created_images_list
                    truncated_data.append(current_created_item)
            json.dump(truncated_data, truncated_file, indent=4)
