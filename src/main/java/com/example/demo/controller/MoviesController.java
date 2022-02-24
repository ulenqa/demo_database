package com.example.demo.controller;
import com.example.demo.entity.EmployeeEntity;
import com.example.demo.entity.MoviesEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("movies")
public class MoviesController {

    static EntityManagerFactory entityManagerFactory;
    static EntityManager entityManager;

    static {
        entityManagerFactory =
                Persistence.createEntityManagerFactory("default");
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response setMovie(MoviesEntity movies) {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(movies);
            entityManager.getTransaction().commit();
            entityManager.close();
            return Response.ok(movies).build();
        }
    @DELETE
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response deleteMovie (@PathParam("id") String id) {
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
            Object movie = entityManager.find(MoviesEntity.class, ID);
            if (movie == null) {
                return Response.status(404, "NotFound: Car with id = " + ID + " not found in the database").build();
            }
            entityManager.getTransaction().begin();
            entityManager.remove(movie);
            entityManager.getTransaction().commit();
            entityManager.close();
            return Response.ok(movie).build();
        } catch (Exception e) {
            return Response.status(500, "InternalServerError").build();
        }
    }
    @GET
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getMovie(@PathParam("id") String id){
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
            MoviesEntity movie = entityManager.find(MoviesEntity.class,ID);
            entityManager.close();
            if (movie==null){
                return Response.status(404, "NotFound: Movie with id = " + ID + " not found in the database").build();
            }
            return Response.ok(movie).build();
        }catch (Exception e){
            return Response.status(500, "InternalServerError").entity(e.getMessage()).build();
        }
    }

}
