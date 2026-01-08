package store;

import store.controller.ConvenienceController;
import store.global.config.DIConfig;

public class Application {
    public static void main(String[] args) {
        DIConfig diConfig = new DIConfig();
        ConvenienceController convenienceController = diConfig.convenienceController();
        convenienceController.run();
    }
}
