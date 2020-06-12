// @flow

import {tryMetaTagContent} from "./dom/getMetaTagContent";

const loadFeatures = (): ?string => tryMetaTagContent("wishlist-features");

const activeFeatures = (): Array<string> => {
    const features = loadFeatures();

    if (features == null) {
        return [];
    }

    return features.split(",");
};

export default activeFeatures;
