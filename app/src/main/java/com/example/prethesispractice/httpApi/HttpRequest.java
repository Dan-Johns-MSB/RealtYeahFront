package com.example.prethesispractice.httpApi;

import com.example.prethesispractice.entities.Client;
import com.example.prethesispractice.entities.ClientsStatus;
import com.example.prethesispractice.entities.ClientsStatusesAssignment;
import com.example.prethesispractice.entities.Document;
import com.example.prethesispractice.entities.Employee;
import com.example.prethesispractice.entities.EmployeesStatus;
import com.example.prethesispractice.entities.EstateObject;
import com.example.prethesispractice.entities.ObjectsStatus;
import com.example.prethesispractice.entities.Operation;
import com.example.prethesispractice.entities.OperationsStatus;
import com.example.prethesispractice.entities.PubKey;
import com.example.prethesispractice.entities.User;
import com.example.prethesispractice.entities.UserResponse;
import com.example.prethesispractice.models.EncModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
import okhttp3.ResponseBody;

public interface HttpRequest {
    // Users
    @GET("/api/PublicKey")
    Call<PubKey> loadPubKey();
    @POST("/api/Users/authenticate")
    Call<UserResponse> login(@Body EncModel jsonData);
    @POST("/api/Users/register")
    Call<UserResponse> register(@Body EncModel jsonData);
    @GET("/api/Users")
    Call<List<User>> getUsers(@Header("Authorization") String token);
    @GET("/api/Users/{id}")
    Call<User> getUserById(@Header("Authorization") String token, @Path("id") int userId);
    @PUT("/api/Users/{id}")
    Call<ResponseBody> updateUser(@Header("Authorization") String token, @Path("id") int userId, @Body EncModel jsonData);
    @DELETE("/api/Users/{id}")
    Call<ResponseBody> deleteUser(@Header("Authorization") String token, @Path("id") int userId);
    @GET("/api/Users/role/{id}")
    Call<String> getEmployeeUserRole(@Header("Authorization") String token, @Path("id") int employeeId);
    @GET("/api/Users/byEmployee/{id}")
    Call<User> getEmployeeUser(@Header("Authorization") String token, @Path("id") int employeeId);

    // Clients
    @GET("/api/Clients")
    Call<List<Client>> getClients(@Header("Authorization") String token);
    @GET("/api/Clients/{id}")
    Call<Client> getClientById(@Header("Authorization") String token, @Path("id") int clientId);
    @PUT("/api/Clients/{id}")
    Call<ResponseBody> updateClient(@Header("Authorization") String token, @Path("id") int clientId, @Body Client updatedClient);
    @POST("/api/Clients")
    Call<Client> insertClient(@Header("Authorization") String token, @Body Client insertClient);
    @DELETE("/api/Clients/{id}")
    Call<ResponseBody> deleteClient(@Header("Authorization") String token, @Path("id") int clientId);
    @GET("/api/Clients/statuses/{status}")
    Call<List<Client>> getClientsByStatus(@Header("Authorization") String token, @Path("status") String status);
    @GET("/api/Clients/passport/{passportNumber}")
    Call<Client> getClientByPassport(@Header("Authorization") String token, @Path("id") String passportNumber);
    @GET("/api/Clients/{id}/objects")
    Call<List<EstateObject>> getClientRelatedObjects(@Header("Authorization") String token, @Path("id") int clientId);
    @GET("/api/Clients/{id}/objects/latestRelated")
    Call<EstateObject> getClientLatestObject(@Header("Authorization") String token, @Path("id") int clientId);
    @GET("/api/Clients/{id}/objects/latestRelatedAddress")
    Call<String> getClientLatestObjectAddress(@Header("Authorization") String token, @Path("id") int clientId);
    @GET("/api/Clients/{id}/buyRequirements")
    Call<List<ClientsStatusesAssignment>> getBuyRequirements(@Header("Authorization") String token, @Path("id") int clientId);
    @GET("/api/Clients/{id}/sellRequirements")
    Call<List<ClientsStatusesAssignment>> getSellRequirements(@Header("Authorization") String token, @Path("id") int clientId);
    @GET("/api/Clients/{id}/rentRequirements")
    Call<List<ClientsStatusesAssignment>> getRentRequirements(@Header("Authorization") String token, @Path("id") int clientId);
    @GET("/api/Clients/{id}/forRentRequirements")
    Call<List<ClientsStatusesAssignment>> getForRentRequirements(@Header("Authorization") String token, @Path("id") int clientId);
    @GET("/api/Clients/{id}/operations")
    Call<List<Operation>> getClientOperations(@Header("Authorization") String token, @Path("id") int clientId);

    // Clients Statuses
    @GET("/api/ClientsStatus")
    Call<List<ClientsStatus>> getClientsStatuses(@Header("Authorization") String token);
    @GET("/api/ClientsStatus/{id}")
    Call<ClientsStatus> getClientsStatus(@Header("Authorization") String token, @Path("id") String status);
    @PUT("/api/ClientsStatus/{id}")
    Call<ResponseBody> updateClientsStatus(@Header("Authorization") String token, @Path("id") String status, @Body ClientsStatus updatedStatus);
    @POST("/api/ClientsStatus")
    Call<ClientsStatus> insertClientsStatus(@Header("Authorization") String token, @Body ClientsStatus insertStatus);
    @DELETE("/api/ClientsStatus/{id}")
    Call<ResponseBody> deleteClientsStatus(@Header("Authorization") String token, @Path("id") String status);

    // Clients Statuses Assignments
    @GET("/api/ClientsStatusesAssignments")
    Call<List<ClientsStatusesAssignment>> getClientsStatusesAssignments(@Header("Authorization") String token);
    @GET("/api/ClientsStatusesAssignments/{client}/{status}/{operation}")
    Call<ClientsStatusesAssignment> getClientsStatusesAssignment(@Header("Authorization") String token,
                                                                 @Path("client") int clientId,
                                                                 @Path("status") String status,
                                                                 @Path("operation") int operationId);
    @PUT("/api/ClientsStatusesAssignments/{client}/{status}/{operation}")
    Call<ResponseBody> updateClientsStatusesAssignment(@Header("Authorization") String token,
                                                  @Path("client") int clientId,
                                                  @Path("status") String status,
                                                  @Path("operation") int operationId,
                                                  @Body ClientsStatusesAssignment updatedAssignment);
    @POST("/api/ClientsStatusesAssignments")
    Call<ClientsStatusesAssignment> insertClientsStatusesAssignment(@Header("Authorization") String token, @Body ClientsStatusesAssignment insertAssignment);
    @DELETE("/api/ClientsStatusesAssignments/{client}/{status}/{operation}")
    Call<ResponseBody> deleteClientsStatusesAssignment(@Header("Authorization") String token,
                                                  @Path("client") int clientId,
                                                  @Path("status") String status,
                                                  @Path("operation") int operationId);

    // Documents
    @GET("/api/Documents")
    Call<List<Document>> getDocuments(@Header("Authorization") String token);
    @GET("/api/Documents/{id}")
    Call<Document> getDocumentById(@Header("Authorization") String token, @Path("id") int documentId);
    @PUT("/api/Documents/{id}")
    Call<ResponseBody> updateDocument(@Header("Authorization") String token, @Path("id") int documentId, @Body Document updatedDocument);
    @POST("/api/Documents")
    Call<Document> insertDocument(@Header("Authorization") String token, @Body Document insertDocument);
    @DELETE("/api/Documents/{id}")
    Call<ResponseBody> deleteDocument(@Header("Authorization") String token, @Path("id") int documentId);

    // Employees
    @GET("/api/Employees")
    Call<List<Employee>> getEmployees(@Header("Authorization") String token);
    @GET("/api/Employees/{id}")
    Call<Employee> getEmployeeById(@Header("Authorization") String token, @Path("id") int employeeId);
    @PUT("/api/Employees/{id}")
    Call<ResponseBody> updateEmployee(@Header("Authorization") String token, @Path("id") int employeeId, @Body Employee updatedEmployee);
    @POST("/api/Employees")
    Call<Employee> insertEmployee(@Header("Authorization") String token, @Body Employee insertEmployee);
    @DELETE("/api/Employees/{id}")
    Call<ResponseBody> fireEmployee(@Header("Authorization") String token, @Path("id") int employeeId);
    @PUT("/api/Employees/{id}/fireSaved")
    Call<Employee> fireEmployeeSaved(@Header("Authorization") String token, @Path("id") int employeeId);
    @GET("/api/Employees/status/{status}")
    Call<List<Employee>> getEmployeesByStatus(@Header("Authorization") String token, @Path("status") String status);
    @GET("/api/Employees/{id}/email")
    Call<String> getEmployeeEmail(@Header("Authorization") String token, @Path("id") int employeeId);
    @GET("/api/Employees/{id}/relatedOperations")
    Call<List<Operation>> getEmployeeRelatedOperations(@Header("Authorization") String token, @Path("id") int employeeId);
    @GET("/api/Employees/{id}/relatedClients")
    Call<List<Client>> getEmployeeRelatedClients(@Header("Authorization") String token, @Path("id") int employeeId);
    @GET("/api/Employees/{id}/relatedObjects")
    Call<List<EstateObject>> getEmployeeRelatedObjects(@Header("Authorization") String token, @Path("id") int employeeId);

    // Employees Statuses
    @GET("/api/EmployeesStatus")
    Call<List<EmployeesStatus>> getEmployeesStatuses(@Header("Authorization") String token);
    @GET("/api/EmployeesStatus/{id}")
    Call<EmployeesStatus> getEmployeesStatus(@Header("Authorization") String token, @Path("id") String status);
    @PUT("/api/EmployeesStatus/{id}")
    Call<ResponseBody> updateEmployeesStatus(@Header("Authorization") String token, @Path("id") String status, @Body EmployeesStatus updatedStatus);
    @POST("/api/EmployeesStatus")
    Call<EmployeesStatus> insertEmployeesStatus(@Header("Authorization") String token, @Body EmployeesStatus insertStatus);
    @DELETE("/api/EmployeesStatus/{id}")
    Call<ResponseBody> deleteEmployeesStatus(@Header("Authorization") String token, @Path("id") String status);

    // Estate Objects
    @GET("/api/EstateObjects")
    Call<List<EstateObject>> getEstateObjects(@Header("Authorization") String token);
    @GET("/api/EstateObjects/{id}")
    Call<EstateObject> getEstateObjectById(@Header("Authorization") String token, @Path("id") int estateObjectId);
    @PUT("/api/EstateObjects/{id}")
    Call<ResponseBody> updateEstateObject(@Header("Authorization") String token, @Path("id") int estateObjectId, @Body EstateObject updatedEstateObject);
    @POST("/api/EstateObjects")
    Call<EstateObject> insertEstateObject(@Header("Authorization") String token, @Body EstateObject insertEstateObject);
    @DELETE("/api/EstateObjects/{id}")
    Call<ResponseBody> deleteEstateObject(@Header("Authorization") String token, @Path("id") int estateObjectId);
    @GET("/api/EstateObjects/statuses/{status}")
    Call<List<EstateObject>> getEstateObjectsByStatus(@Header("Authorization") String token, @Path("status") String status);
    @GET("/api/EstateObjects/address/{address}")
    Call<EstateObject> getEstateObjectByAddress(@Header("Authorization") String token, @Path("id") String address);
    @GET("/api/EstateObjects/{id}/owner")
    Call<Client> getEstateObjectOwner(@Header("Authorization") String token, @Path("id") int estateObjectId);
    @GET("/api/EstateObjects/{id}/ownerName")
    Call<String> getEstateObjectOwnerName(@Header("Authorization") String token, @Path("id") int estateObjectId);
    @GET("/api/EstateObjects/{id}/interestedClients")
    Call<List<Client>> getEstateObjectInterestedClients(@Header("Authorization") String token, @Path("id") int estateObjectId);
    @GET("/api/EstateObjects/{id}/operations")
    Call<List<Operation>> getEstateObjectOperations(@Header("Authorization") String token, @Path("id") int estateObjectId);
    @GET("/api/EstateObjects/{id}/lastOperation")
    Call<Operation> getEstateObjectLastOperation(@Header("Authorization") String token, @Path("id") int estateObjectId);

    // Objects Statuses
    @GET("/api/ObjectsStatus")
    Call<List<ObjectsStatus>> getObjectsStatuses(@Header("Authorization") String token);
    @GET("/api/ObjectsStatus/{id}")
    Call<ObjectsStatus> getObjectsStatus(@Header("Authorization") String token, @Path("id") String status);
    @PUT("/api/ObjectsStatus/{id}")
    Call<ResponseBody> updateObjectsStatus(@Header("Authorization") String token, @Path("id") String status, @Body ObjectsStatus updatedStatus);
    @POST("/api/ObjectsStatus")
    Call<ObjectsStatus> insertObjectsStatus(@Header("Authorization") String token, @Body ObjectsStatus insertStatus);
    @DELETE("/api/ObjectsStatus/{id}")
    Call<ResponseBody> deleteObjectsStatus(@Header("Authorization") String token, @Path("id") String status);

    // Operations
    @GET("/api/Operations")
    Call<List<Operation>> getOperations(@Header("Authorization") String token);
    @GET("/api/Operations/{id}")
    Call<Operation> getOperationById(@Header("Authorization") String token, @Path("id") int operationId);
    @PUT("/api/Operations/{id}")
    Call<ResponseBody> updateOperation(@Header("Authorization") String token, @Path("id") int operationId, @Body Operation updatedOperation);
    @POST("/api/Operations")
    Call<Operation> insertOperation(@Header("Authorization") String token, @Body Operation insertOperation);
    @DELETE("/api/Operations/{id}")
    Call<ResponseBody> deleteOperation(@Header("Authorization") String token, @Path("id") int operationId);
    @PUT("/api/Operations/{id}/assign/{actType}")
    Call<Operation> assignAct(@Header("Authorization") String token,
                              @Path("id") int operationId,
                              @Path("actType") String actType);
    @PUT("/api/Operations/{id}/cancel")
    Call<Operation> cancelOperation(@Header("Authorization") String token,
                                    @Path("id") int operationId);
    @GET("/api/Operations/bd/{start}/to/{end}")
    Call<List<Operation>> getOperationsByDate(@Header("Authorization") String token,
                                              @Path("start") String start,
                                              @Path("end") String end);
    @GET("/api/Operations/bd/{start}/to/{end}/host/{id}")
    Call<List<Operation>> getOperationsByDateAndHost(@Header("Authorization") String token,
                                              @Path("start") String start,
                                              @Path("end") String end,
                                              @Path("id") int hostId);
    @GET("/api/Operations/host/{id}")
    Call<List<Operation>> getOperationsByHost(@Header("Authorization") String token, @Path("id") int hostId);
    @GET("/api/Operations/{id}/host")
    Call<Employee> getHostByOperation(@Header("Authorization") String token, @Path("id") int operationId);
    @GET("/api/Operations/{id}/Object")
    Call<EstateObject> getOperationObject(@Header("Authorization") String token, @Path("id") int operationId);
    @GET("/api/Operations/{id}/ObjectAddress")
    Call<String> getOperationObjectAddress(@Header("Authorization") String token, @Path("id") int operationId);
    @GET("/api/Operations/{id}/availableActs")
    Call<List<Boolean>> getAvailableActs(@Header("Authorization") String token, @Path("id") int operationId);
    @GET("/api/Operations/{id}/availableForNext")
    Call<Boolean> getNextAvailability(@Header("Authorization") String token, @Path("id") int operationId);
    @GET("/api/Operations/actTypeActive/{actType}")
    Call<List<Operation>> getOperationsByActiveAct(@Header("Authorization") String token, @Path("actType") String actType);

    // Operations Statuses
    @GET("/api/OperationsStatus")
    Call<List<OperationsStatus>> getOperationsStatuses(@Header("Authorization") String token);
    @GET("/api/OperationsStatus/{id}")
    Call<OperationsStatus> getOperationsStatus(@Header("Authorization") String token, @Path("id") String status);
    @PUT("/api/OperationsStatus/{id}")
    Call<ResponseBody> updateOperationsStatus(@Header("Authorization") String token, @Path("id") String status, @Body OperationsStatus updatedStatus);
    @POST("/api/OperationsStatus")
    Call<OperationsStatus> insertOperationsStatus(@Header("Authorization") String token, @Body OperationsStatus insertStatus);
    @DELETE("/api/OperationsStatus/{id}")
    Call<ResponseBody> deleteOperationsStatus(@Header("Authorization") String token, @Path("id") String status);
}
