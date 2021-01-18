package quarkus.grpc;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Singleton;

import grpc.MutinyPersonGrpc;
import grpc.PersonRequest;
import grpc.PersonReply;
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
    public Uni<PersonReply> echoPerson(PersonRequest person) {
        int count = counter.incrementAndGet();
        PersonReply personReply = PersonReply.newBuilder()
                        .setName(person.getName())
                        .setSurname(person.getSurname())
                        .setAge(person.getAge())
                        .setAdult(isAdult(person.getAge()))
                        .setCount(count)
                        .build();
        return Uni.createFrom().item(personReply);
    }
}
