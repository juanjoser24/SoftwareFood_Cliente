/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.client.access;

import co.unicauca.restaurant.client.infra.RestaurantSocket;
import co.unicauca.restaurant.commons.domain.PlatoEjecutivo;
import co.unicauca.restaurant.commons.domain.Restaurant;
import co.unicauca.restaurant.commons.infra.JsonError;
import co.unicauca.restaurant.commons.infra.Protocol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author braia
 */
public class ClientAccessSocket implements IClientAccess {

    /**
     * uso de un socket para comunicarse con el servidor
     */
    private RestaurantSocket mySocket;

    //el costructor crea el socket para poder comunicarse con el servidor
    public ClientAccessSocket() {
        mySocket = new RestaurantSocket();
    }

    /**
     * establece la conexion con el servidor por parametro
     *
     * @param requestJson solicitud al servidor
     * @return verdadero si la solicitud es exitosa, falsa de lo contrario
     * @throws Exception
     */
    private String processConnection(String requestJson) throws Exception {
        String jsonResponse = null;
        try {
            //se establece la conexion
            mySocket.connect();
            //se envia la solicitud y se recibe una respuesta
            jsonResponse = mySocket.sendStream(requestJson);
            mySocket.closeStream();
            mySocket.disconnect();
            if (jsonResponse.equals("FALLO")) {
                return "FALLO";
            } else {
                System.out.println("Inició");
            }
        } catch (IOException ex) {
            ex.getMessage();
        }
        if (jsonResponse == null) {
            throw new Exception("error al conectarse con el servidor");
        } else {
            if (jsonResponse.contains("error")) {
                //Devolvió error, realizar login
                System.out.println("hubo error");
                throw new Exception(this.extractMessages(jsonResponse));
            } else {
                //Devuelve la respuesta del servidor
                return jsonResponse;
            }
        }
    }

    /**
     * Extra los mensajes de la lista de errores
     *
     * @param jsonResponse lista de mensajes json
     * @return Mensajes de error
     */
    private String extractMessages(String jsonResponse) {
        JsonError[] errors = jsonToErrors(jsonResponse);
        String msjs = "";
        for (JsonError error : errors) {
            msjs += error.getMessage();
        }
        return msjs;
    }

    /**
     * Convierte el jsonError a un array de objetos jsonError
     *
     * @param jsonError
     * @return objeto MyError
     */
    private JsonError[] jsonToErrors(String jsonError) {
        Gson gson = new Gson();
        JsonError[] error = gson.fromJson(jsonError, JsonError[].class);
        return error;
    }

    /**
     * crea el protocolo en formato Json de la informacion que se desea enviar
     *
     * @param instance
     * @return
     */
    private String createRestaurantJson(Restaurant instance) {
        Protocol protocol = new Protocol();
        //el orden debe ser respetado
        protocol.setResource("administrador");
        protocol.setAction("postRestaurante");
        protocol.addParameter("res_nit", String.valueOf(instance.getNit()));
        protocol.addParameter("nombre", instance.getNombre());

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);
        return requestJson;
    }

    /**
     * se crea el protocolo de comunicacion
     *
     * @param instance plato al que se el enviara la informacion
     * @return
     */
    private String createPlatoEjecutivoJson(PlatoEjecutivo instance) {
        Protocol protocol = new Protocol();
        protocol.setResource("administrador");
        protocol.setAction("postPlatoEjecutivo");
        protocol.addParameter("peje_id", String.valueOf(instance.getId()));
        protocol.addParameter("mdia_id", String.valueOf(instance.getMenuId()));
        protocol.addParameter("nombre", instance.getNombre());
        protocol.addParameter("descripcion", instance.getDescripcion());
        protocol.addParameter("dia", String.valueOf(instance.getDiaSemana()));
        protocol.addParameter("entrada", instance.getEntrada());
        protocol.addParameter("principio", instance.getPrincipio());
        protocol.addParameter("proteina", instance.getProteina());
        protocol.addParameter("bebida", instance.getBebida());
        protocol.addParameter("precio", String.valueOf(instance.getPrecio()));

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);
        return requestJson;
    }

    public String updatePlatoEjecutivoJson(int clave, String atributo, String valor) {
        Protocol protocol = new Protocol();
        //el orden debe ser respetado
        protocol.setResource("administrador");
        protocol.setAction("updatePlatoEjecutivo");
        protocol.addParameter("clave", "" + clave);
        protocol.addParameter("atributo", atributo);
        protocol.addParameter("valor", valor);

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json enviado: " + requestJson);
        return requestJson;
    }

    /**
     * Crea el plato recibido en un json para el envio por el sockect al
     * servidor
     *
     * @param resource
     * @param accion
     * @param resNit
     * @return
     */
    private String createlistMenuJson(String resource, String accion, int resNit) {
        Protocol protocol = new Protocol();
        protocol.setResource(resource);
        protocol.setAction(accion);
        protocol.addParameter("resNit", String.valueOf(resNit));

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);
        return requestJson;
    }

    /**
     * Convierte un json en una lista de tipo plato ejecutivo
     *
     * @param jsonListarMenu
     * @return
     */
    private List<PlatoEjecutivo> jsonListarMenuDia(String jsonListarMenu) {
        Gson gson = new Gson();
        Type list = new TypeToken<List<PlatoEjecutivo>>() {
        }.getType();
        return gson.fromJson(jsonListarMenu, list);
    }

    /* envia la solicitud al servidor para eliminar un plato ejecutivo 
     * @param idPlaD id del plato ejecutivo que se quiere eliminar
     * @return
     * @throws Exception
     */
    @Override
    public String deletePlatoEjecutivo(int idPlaE) throws Exception {
        String respJson = deletePlatoEjecutivoJson(idPlaE);
        if (this.processConnection(respJson).equals("FALLO")) {
            return "FALLO";
        }
        return "" + idPlaE;
    }

    /**
     * se crea el protocolo de comunicacion
     *
     * @param idPlaE
     * @return
     * @throws Exception
     */
    public String deletePlatoEjecutivoJson(int idPlaE) throws Exception {
        Protocol protocol = new Protocol();
        protocol.setResource("administrador");
        protocol.setAction("eliminarPlatoEjecutivo");
        protocol.addParameter("id", "" + idPlaE);

        Gson gson = new Gson();
        String requestJson = gson.toJson(protocol);
        System.out.println("json: " + requestJson);

        return requestJson;
    }

    /**
     * Envia el id de un restaurante y devuelve la lista llegada desde el
     * servidor el cual transforma el json recibido desde este en una lista de
     * PLlato dia que conforma un menu por dias
     *
     * @param resNit
     * @return
     * @throws Exception
     */
    @Override
    public List<PlatoEjecutivo> listarMenuDia(int resNit) throws Exception {
        String resource = "administrador";
        String accion = "listarMenuDia";
        String requestJson = createlistMenuJson(resource, accion, resNit);
        String response = processConnection(requestJson);
        return jsonListarMenuDia(response);
    }

    /**
     * envia la solicitud al servidor para guardar un restaurante
     *
     * @param res la instancia a guardar
     * @return (hacer validacion con el retorno)
     * @throws Exception
     */
    @Override
    public String saveRestaurant(Restaurant res) throws Exception {

        String requestJson = createRestaurantJson(res);
        if ((this.processConnection(requestJson)).equals("FALLO")) {
            return null;
        }
        return res.getNombre();
    }

    @Override
    public String savePlatoEjecutivo(PlatoEjecutivo instance) throws Exception {
        //devuelve un string en formato Json que lo que se enviara
        String requestJson = createPlatoEjecutivoJson(instance);
        if ((this.processConnection(requestJson)).equals("FALLO")) {
            return null;
        }
        return instance.getNombre();
    }

    @Override
    public boolean updatePlatoEjecutivo(int clave, String atributo, String valor) throws Exception {
        String requestJson = updatePlatoEjecutivoJson(clave, atributo, valor);
        if (processConnection(requestJson).equals("FALLO")) {
            return false;
        }
        return true;
    }

}
