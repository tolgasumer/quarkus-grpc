package quarkus.grpc;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Singleton;

import grpc.MutinyItemGrpc;
import grpc.ItemRequest;
import grpc.ItemResponse;
import io.smallrye.mutiny.Uni;

@Singleton
public class ItemService extends MutinyItemGrpc.ItemImplBase {
    AtomicInteger counter = new AtomicInteger();

    public boolean isFit(int width, int length) {
        if((width*length) <= 100) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Uni<ItemResponse> echoItem(ItemRequest item) {
        int count = counter.incrementAndGet();
        ItemResponse itemResponse = ItemResponse.newBuilder()
                .setName(item.getName())
                .setWidth(item.getWidth())
                .setLength(item.getLength())
                .setFit(isFit(item.getWidth(), item.getLength()))
                .setCount(count)
                .build();
        return Uni.createFrom().item(itemResponse);
    }
}
