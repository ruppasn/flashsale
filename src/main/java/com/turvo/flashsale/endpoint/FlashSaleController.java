package com.turvo.flashsale.endpoint;

import com.turvo.flashsale.service.flashsale.IFlashSaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/flashsale")
public class FlashSaleController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFlashSaleService flashSaleService;

    @GetMapping("start")
    public boolean start() {
        log.info("flash sale started");
        flashSaleService.startFlashSale();
        return true;
    }

    @GetMapping("stop")
    public boolean stop() {
        log.info("flash sale stopped");
        flashSaleService.stopFlashSale();
        return true;
    }

}
