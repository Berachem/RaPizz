package com.example.rapizzapp.handlers;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLoaderTask extends Task<Image> {
    private final String imageUrl;

    public ImageLoaderTask(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    protected Image call() throws Exception {
        return new Image(imageUrl, true);
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        // Here you can handle the loaded image if needed
    }

    @Override
    protected void failed() {
        super.failed();
        // Handle failure if needed
    }
}
