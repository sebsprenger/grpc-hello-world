package de.sebsprenger.grpc.helloworld;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void testOrder() {
        var article1 = Article.newBuilder()
                .setArticleId("1234")
                .setName("T-Shirt")
                .setDescription("A really nice black t-shirt")
                .setPriceInSmallestUnit(2699)
                .setCurrency(Currency.EUR)
                .build();

        var article2 = Article.newBuilder()
                .setArticleId("2345")
                .setName("Jacket")
                .setDescription("Superwarm jacket for polar winter")
                .setPriceInSmallestUnit(36999)
                .setCurrency(Currency.EUR);

        var payment = PayPal.newBuilder()
                .setPayPalId("987")
                .setOtherDetail("@user.name")
                .build();

        var invoiceAddress = InvoiceAddress.newBuilder()
                .setFirstname("Seb")
                .setLastname("Sprenger")
                .setStreet("Example St.")
                .setStreetnumber("1430")
                .setZipcode("70173")
                .setCity("Stuttgart")
                .setState("BW")
                .setCountry("Germany")
                .build();

        var deliveryAddress1 = PrivateDeliveryAddress.newBuilder()
                .setFirstname("Seb")
                .setLastname("Sprenger")
                .setStreet("Example St.")
                .setStreetnumber("1430")
                .setZipcode("70173")
                .setCity("Stuttgart")
                .setState("BW")
                .setCountry("Germany")
                .build();

        var deliveryAddress2 = ClickAndCollectAddress.newBuilder()
                .setCustomerId(CustomerId.newBuilder().setCustomerId("123"))
                .setDepartmentStore(ClickAndCollectRetrievalPointId.newBuilder()
                .setClickAndCollectRetrievalPointId("10"))
                .build();

        var underTest = Order.newBuilder()
                .setOrderId(OrderId.newBuilder().setOrderId("123456789"))
                .addArticles(article1)
                .addArticles(article2)
                .setPayPal(payment)
                .setInvoiceAddress(invoiceAddress)
                .setPrivateAddress(deliveryAddress1)
                .setClickAndCollectAddress(deliveryAddress2)
                .build();


        assertThat(underTest.getPayPal().getPayPalId())
                .isEqualTo("987");

        assertThat(underTest.getDeliveryAddressCase())
                .isEqualTo(Order.DeliveryAddressCase.CLICKANDCOLLECTADDRESS);

        assertThat(underTest.getPrivateAddress())
                .isEqualTo(underTest.getPrivateAddress().getDefaultInstanceForType());

        assertThat(underTest.getClickAndCollectAddress())
                .isEqualTo(deliveryAddress2);

        assertThat(underTest.hasGiftWrapping())
                .isFalse();
        assertThat(underTest.getGiftWrapping())
                .isFalse();
    }
}
