package store.global.config;

import store.controller.ConvenienceController;
import store.domain.model.Order;
import store.domain.repository.OrderRepository;
import store.domain.repository.ProductPromotionRepository;
import store.domain.repository.ProductRepository;
import store.domain.repository.PromotionRepository;
import store.domain.service.ConvenienceService;
import store.domain.service.FileService;
import store.global.util.Parser;
import store.global.util.StringParser;
import store.view.InputView;
import store.view.OutputView;

public final class DIConfig {

    private final ProductRepository productRepository = new ProductRepository();
    private final PromotionRepository promotionRepository = new PromotionRepository();
    private final ProductPromotionRepository productPromotionRepository = new ProductPromotionRepository();
    private final OrderRepository orderRepository = new OrderRepository();


    public ConvenienceController convenienceController() {
        return new ConvenienceController(
                fileService(),
                convenienceService(),
                inputView(),
                outputView()
        );
    }

    public FileService fileService() {
        return new FileService(
                productRepository(),
                promotionRepository(),
                productPromotionRepository(),
                stringParser()
        );
    }

    public ConvenienceService convenienceService() {
        return new ConvenienceService(
                productRepository(),
                promotionRepository(),
                productPromotionRepository(),
                orderRepository(),
                stringParser()
        );
    }

    public Parser<String> stringParser() {
        return new StringParser();
    }

    public InputView inputView() {
        return new InputView();
    }

    public OutputView outputView() {
        return new OutputView();
    }

    public ProductRepository productRepository() {
        return productRepository;
    }

    public PromotionRepository promotionRepository() {
        return promotionRepository;
    }

    public ProductPromotionRepository productPromotionRepository() {
        return productPromotionRepository;
    }

    public OrderRepository orderRepository() {
        return orderRepository;
    }
}
