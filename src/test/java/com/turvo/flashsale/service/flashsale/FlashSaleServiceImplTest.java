package com.turvo.flashsale.service.flashsale;

import com.turvo.flashsale.dto.OrderRequest;
import com.turvo.flashsale.exception.IlligallFlashSaleAccess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlashSaleServiceImplTest {

    @InjectMocks
    private FlashSaleServiceImpl flashSaleService;

    @Mock
    private FlashSaleManager flashSaleManager;

    @Test
    public void onFlashSaleStartShouldLoadCustomersAndInventory() {
        flashSaleService.startFlashSale();
        verify(flashSaleManager).loadCustomers();
        verify(flashSaleManager).loadInventory();
        verify(flashSaleManager).startSale();
    }

    @Test(expected = IlligallFlashSaleAccess.class)
    public void shouldNotAllowToOrderIfFlashSaleDoesNotStart() {
        flashSaleService.order(mock(OrderRequest.class));
        verify(flashSaleManager, never()).order(any());
    }

    @Test
    public void allowToOrderOnlyIfFlashSaleStart() {
        when(flashSaleManager.isFlashSaleActive()).thenReturn(true);
        flashSaleService.order(mock(OrderRequest.class));
        verify(flashSaleManager).order(any());
    }

    @Test
    public void stopFlashSaleShouldMakeFlashSaleInActive() {
        flashSaleService.stopFlashSale();
        verify(flashSaleManager).stopSale();
        assertFalse(flashSaleManager.isFlashSaleActive());
    }

}