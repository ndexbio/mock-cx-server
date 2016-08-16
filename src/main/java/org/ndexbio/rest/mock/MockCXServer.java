package org.ndexbio.rest.mock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/network")
public class MockCXServer 
{

	@POST
	@Path("/asCX")
	@Produces("application/json")
	@Consumes("multipart/form-data")
	public String createCXNetwork(MultipartFormDataInput input) throws Exception 
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
	@Path("/asCX")
	@Produces("application/json")
	public String getCXNetwork() throws Exception 
	{
		return "Nice job bro!";
	}
}