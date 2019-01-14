import json

NO_ITEMS = 5
DET_INGRS_PATH = "../../../data/det_ingrs.json"

if __name__ == "__main__":
    with open("truncated-det-ingrs-specialized.json", "w") as truncated_det_ingrs_file:
        with open("truncated-layer2-specialized.json") as truncated_layer2_file:
            with open(DET_INGRS_PATH) as det_ingrs_file:
                layer2_data = json.load(truncated_layer2_file)
                det_ingrs_data = json.load(det_ingrs_file)

                truncated_data = []
                for layer2_item in layer2_data:
                    matching_items = [x for x in det_ingrs_data if x['id'] == layer2_item['id']]
                    if len(matching_items) > 0:
                        truncated_data.append(matching_items[0])

                json.dump(truncated_data, truncated_det_ingrs_file, indent=4)
