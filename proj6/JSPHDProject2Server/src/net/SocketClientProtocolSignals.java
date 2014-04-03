package net;

public interface SocketClientProtocolSignals {
	
	final String sendPropertiesRequest = 	"Properties";
	final String listeningForProperties = 	"Listen4Prop";
	final String builtPropertiesSuccess =	"BuiltPropSuc";
	final String builtPropertiesFailure =	"BltPropFail";
	final String queryModelNames = 			"qModelNames";
	final String requestModelBackToClient = "getModel";
	final String notFoundModel = 			"NotFoundMdl";
	final String foundModel = 				"FoundModel";
	final String listeningForModel = 		"Listen4Mdl";
	final String updateModelRequest = 		"Models";
	final String updateModelSuccess =		"UpdateSuc";
	final String updateModelFailure =		"UpdateFail";
	final String queryModelDetails = 		"qModelDetails";
	final String queryOptionSetNames = 		"qOptionNames";
	final String queryOptionNames = 		"qOptvalues";
	final String updateModel = 				"updateModel";

}
