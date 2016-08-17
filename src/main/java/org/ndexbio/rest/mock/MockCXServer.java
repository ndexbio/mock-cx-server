package org.ndexbio.rest.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/network")
public class MockCXServer 
{
	@GET
	@Path("/status")
	@Produces("application/json")
	public String getStatus() throws Exception 
	{
		return "Nice job bro!";
	}
	

	@POST
	@Path("/")
	@Produces("application/json")
	@Consumes("multipart/form-data")
	public String createNetwork(MultipartFormDataInput input) throws Exception 
	{
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("CXNetworkStream");
		UUID uuid = UUID.randomUUID();
		File file = new File("cx-files/" + uuid.toString());
		OutputStream out = new FileOutputStream(file);
		for (InputPart inputPart : inputParts) 
		{
			InputStream in = inputPart.getBody(InputStream.class, null);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) 
			{
				out.write(buf, 0, len);
			}
			in.close();
		}
		out.close();
		return uuid.toString();
	}
	
	@GET
	@Path("/{networkId}")
	@Produces("application/json")
	public Response getNetwork(@PathParam("networkId") final String networkId) throws Exception 
	{
		File cxFile = new File("cx-files/" + networkId);
		if( !cxFile.exists() )
			return Response.status(404).build();
		FileInputStream is = new FileInputStream(cxFile);
		return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(is).build();
	}
	
	@DELETE
	@Path("/{networkId}")
	@Produces("application/json")
	public Response deleteNetwork(@PathParam("networkId") final String networkId) throws Exception 
	{
		File cxFile = new File("cx-files/" + networkId);
		if( !cxFile.exists() )
			return Response.status(404).build();
		boolean success = cxFile.delete();
		if( !success )
			return Response.status(500).build();
		String responseMsg = networkId + " successfully deleted";
		return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(responseMsg).build();
	}
	
}