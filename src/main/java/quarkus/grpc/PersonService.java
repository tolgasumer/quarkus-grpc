package quarkus.grpc;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Singleton;

import grpc.MutinyPersonGrpc;
import grpc.PersonRequest;
import grpc.PersonResponse;
import io.smallrye.mutiny.Uni;

@Singleton
public class PersonService extends MutinyPersonGrpc.PersonImplBase {

    AtomicInteger counter = new AtomicInteger();

    public boolean isAdult(int age) {
        if(age >= 18) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Uni<PersonResponse> echoPerson(PersonRequest person) {
        int count = counter.incrementAndGet();
        PersonResponse personResponse = PersonResponse.newBuilder()
                        .setName(person.getName())
                        .setSurname(person.getSurname())
                        .setAge(person.getAge())
                        .setAdult(isAdult(person.getAge()))
                        .setCount(count)
                        .build();
        return Uni.createFrom().item(personResponse);
    }
}
