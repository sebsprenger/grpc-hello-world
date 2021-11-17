package de.sebsprenger.grpc.helloworld.client;

import de.sebsprenger.grpc.helloworld.Article;
import de.sebsprenger.grpc.helloworld.ClickAndCollectAddress;
import de.sebsprenger.grpc.helloworld.ClickAndCollectRetrievalPointId;
import de.sebsprenger.grpc.helloworld.Currency;
import de.sebsprenger.grpc.helloworld.CustomerId;
import de.sebsprenger.grpc.helloworld.InvoiceAddress;
import de.sebsprenger.grpc.helloworld.OrderDetails;
import de.sebsprenger.grpc.helloworld.PayPal;
import de.sebsprenger.grpc.helloworld.PrivateDeliveryAddress;

public class OrderFactory {

    static OrderDetails order1() {
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

        var orderDetails = OrderDetails.newBuilder()
                .addArticles(article1)
                .addArticles(article2)
                .setPayPal(payment)
                .setInvoiceAddress(invoiceAddress)
                .setPrivateAddress(deliveryAddress1)
                .setClickAndCollectAddress(deliveryAddress2)
                .build();

        return orderDetails;
    }
}
