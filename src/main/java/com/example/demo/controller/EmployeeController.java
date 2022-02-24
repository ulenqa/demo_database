package com.example.demo.controller;

import com.example.demo.entity.EmployeeEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;

@Path("employee")
public class EmployeeController {

    static EntityManagerFactory entityManagerFactory;
    static EntityManager entityManager;

    static {
        entityManagerFactory =
                Persistence.createEntityManagerFactory("default");

    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response setEmployee(EmployeeEntity employee) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(employee);
            entityManager.getTransaction().commit();
            entityManager.close();
            return Response.ok(employee).build();
        } catch (Exception e) {
            return Response.status(500, "InternalServerError").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response deleteEmployee(@PathParam("id") String id) {
        int ID = -1;
        try {
            ID = Integer.parseInt(id);
            if (ID < 1) {
                return Response.status(400, "BadRequest: Id should be a positive integer").build();
            }
        } catch (Exception e) {
            return Response.status(400, "BadRequest: Id should be a positive integer").build();
        }
        try {
            entityManager = entityManagerFactory.createEntityManager();
            Object employee = entityManager.find(EmployeeEntity.class, ID);
            if (employee == null) {
                return Response.status(404, "NotFound: Employee with id = " + ID + " not found in the database").build();
            }
            entityManager.getTransaction().begin();
            entityManager.remove(employee);
            entityManager.getTransaction().commit();
            entityManager.close();
            return Response.ok(employee).build();
        } catch (Exception e) {
            return Response.status(500, "InternalServerError").build();
        }
    }

    @PUT
    @Path("")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateEmployee(EmployeeEntity employee, @HeaderParam("RequestID") String requestID) {
        int ID = employee.getId();
        try {
            if (ID < 1) {
                return Response.status(400, "BadRequest: Id should be a positive integer").header("RequestID", requestID).build();
            }
        } catch (Exception e) {
            return Response.status(400, "BadRequest: Id should be a positive integer").header("RequestID", requestID).build();
        }
        try {
            entityManager = entityManagerFactory.createEntityManager();
            EmployeeEntity oldEmployee = entityManager.find(EmployeeEntity.class, ID);
            if (employee == null || oldEmployee == null) {
                return Response.status(404, "NotFound: Employee with id = " + ID + " not found in the database").header("RequestID", requestID).build();
            }
            entityManager.getTransaction().begin();
            Query query = entityManager.createNativeQuery("UPDATE employee SET fname=\'" + employee.getFname() + "\', lname=\'" + employee.getLname() +
                    "\' WHERE id=" + ID);
            query.executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
            return Response.ok(employee).header("RequestID", requestID).build();
        }   catch (Exception e) {
            return Response.status(500, "InternalServerError").entity(e.getMessage()).header("RequestID", requestID).build();
        }
    }

    @GET
    @Path("/range/{fromid}/{toid}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getEmployeesInRange(@PathParam("fromid") int fromid, @PathParam("toid") int toid) {
        try {
            if(fromid < 1 || toid < 1) {
                return Response.status(400,"BadRequest: Id must be a positive number greater than zero").build();
            }
            if(fromid > toid) {
                return Response.status(400,"BadRequest: Invalid constraint values").build();
            }
        }
        catch(Exception e) {
            return Response.status(400,"BadRequest: Id must be a positive number greater than zero").build();
        }
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List checks = entityManager.createNativeQuery("SELECT * FROM employee e WHERE e.id >= "+fromid+ " AND e.id <= " + toid).getResultList();
            entityManager.close();
            return Response.ok(checks).build();
        } catch (Exception e) {
            return Response.status(500,"InternalServerError ").entity(e.getMessage()).build();

        }
    }
    @GET
    @Path("/askme/{someSenselessQuestion}")
    public Response redirectToGoogle(@PathParam("someSenselessQuestion") String query){
        try{
            URI redirectLink = new URI("https://letmegooglethat.com/?q=" + query);
            return Response.temporaryRedirect(redirectLink).build();
        } catch (Exception e){
            return Response.status(500, "InternalServerError").entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getEmployee(@PathParam("id") String id){
        int ID=-1;
        try{
            ID=Integer.parseInt(id);
            if (ID<1){
                 return Response.status(400,"BadRequest: Id must be a positive number greater than zero").build();
            }
        }catch (Exception e){
            return Response.status(400,"BadRequest: Id must be a positive number greater than zero").build();
        }
        try{
            entityManager=entityManagerFactory.createEntityManager();
            EmployeeEntity employee = entityManager.find(EmployeeEntity.class,ID);
            entityManager.close();
            if (employee==null){
                return Response.status(404, "NotFound: Employee with id = " + ID + " not found in the database").build();
            }
            return Response.ok(employee).build();
        }catch (Exception e){
            return Response.status(500, "InternalServerError").entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/hrdepartment/{id}")
    public Response redirectEmployee(@PathParam("id") String id){
        try{
            URI redirectLink = new URI("http://localhost:8080/demo-1.0-SNAPSHOT/api/employee/" + id);
            return Response.temporaryRedirect(redirectLink).build();
        } catch (Exception e){
            return Response.status(500, "InternalServerError").entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/image/png")
    @Produces("image/png")
    public Response rick(){
        try {
            return Response.ok(
                    getClass().getClassLoader().getResourceAsStream("rick-astley.png")
            ).build();
        } catch (Exception e){
            String error_html = "404, rick-astley.png not found";
            return Response.status(404,"NotFound").entity(new ByteArrayInputStream(error_html.getBytes())).build();
        }
    }

    @GET
    @Path("/image/astley")
    @Produces("image/png")
    public Response getImage() {
        try {
            return Response.ok(getClass().getClassLoader().getResourceAsStream("rick-astley.png")).build();
        } catch (Exception e) {
            return Response.status(404,"Image not found!").build();
        }
    }

    }