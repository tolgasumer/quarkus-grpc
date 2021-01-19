package quarkus.grpc;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import grpc.PersonGrpc;
import grpc.MutinyPersonGrpc;
import grpc.PersonRequest;
import grpc.PersonResponse;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.smallrye.mutiny.Uni;

@Path("/person")
public class PersonEndpoint {

    @Inject
    @GrpcService("person")
    PersonGrpc.PersonBlockingStub blockingPersonService;
    @Inject
    @GrpcService("person")
    MutinyPersonGrpc.MutinyPersonStub mutinyPersonService;

    @GET
    @Path("/blocking")
    public String personBlocking(@QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("age") int age) {
        PersonResponse reply = blockingPersonService.echoPerson((PersonRequest.newBuilder()
                .setName(name)
                .setSurname(surname)
                .setAge(age)
                .build()));
        return generateResponse(reply);
    }

    @GET
    @Path("/mutiny")
    public Uni<String> personMutiny(@QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("age") int age) {
        Uni<PersonResponse> reply = mutinyPersonService.echoPerson(PersonRequest.newBuilder()
                .setName(name)
                .setSurname(surname)
                .setAge(age)
                .build());
        return reply.onItem().transform((res) -> generateResponse(res));
    }

    public String generateResponse(PersonResponse response) {
        return String.format(response.toString());
    }
}
