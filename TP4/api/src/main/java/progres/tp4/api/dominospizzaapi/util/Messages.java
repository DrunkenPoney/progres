package progres.tp4.api.dominospizzaapi.util;

public final class Messages {
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////// BASE OBJECTS ATTRIBUTES //////////////////////////////////////////////
	
	public static final String MSG_INVALID_KEY         = "L'attribut «key» peut contenir uniquement des caractères "
		                                                     + "alphanumériques et des underscores (_)";
	public static final String MSG_INVALID_POSTAL_CODE = "L'attribut «postalCode» est invalide";
	public static final String MSG_INVALID_PHONE       = "L'attribute «phone» est invalide";
	public static final String MSG_VOLUME_MIN_ZERO     = "L'attribut «volume» doit être un nombre plus grand ou égal à 0";
	public static final String MSG_COST_MIN_ZERO       = "L'attribut «cost» doit être un nombre plus grand ou égal à 0";
	public static final String MSG_MODIFIER_MIN_ZERO   = "L'attribut «modifier» doit être un nombre plus grand ou égal à 0";
	
	public static final String MSG_BLANK_KEY        = "L'attribut «key» est vide";
	public static final String MSG_BLANK_FIRST_NAME = "L'attribut «firstName» est vide";
	public static final String MSG_BLANK_LAST_NAME  = "L'attribut «lastName» est vide";
	public static final String MSG_BLANK_ADDRESS    = "L'attribut «address» est vide";
	public static final String MSG_BLANK_CITY       = "L'attribut «city» est vide";
	public static final String MSG_BLANK_TITLE      = "L'attribut «title» est vide";
	public static final String MSG_BLANK_UNIT       = "L'attribut «unit» est vide";
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////// REST VALIDATIONS /////////////////////////////////////////////////
	
	public static final String MSG_REQUIRED_PARAM       = "Le paramètre %s de la requête est obligatoire";
	public static final String MSG_REQUIRED_PARAMS      = "Les paramètres %s de la requête sont obligatoires";
	public static final String MSG_INVALID_PARAM_ID     = "Le paramètre «ID» de la requête est invalide";
	public static final String MSG_MISSING_BODY         = "Le corp de la requête est manquant";
	public static final String MSG_REQUIRED_ID          = "L'attribut «ID» est requis";
	
	public static final String MSG_MISSING_ATTR        = "L'attribut «%s» est obligatoire";
	public static final String MSG_MISSING_ENTITY_ATTR = "L'attribut «%s» de l'entité «%s» est obligatoire";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////  /////////////////////////////////////////////////////////
}
