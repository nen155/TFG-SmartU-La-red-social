<?php
class Usuario{
	//Si tengo el paramétro json significa que lo quiero convertir de un json
	 public function __construct($json = false) {
        if ($json) $this->set(json_decode($json, true));
    }
	//Convierte del JSON a el objeto que tengo
    public function set($data) {
        foreach ($data AS $key => $value) {
            /*Se supon que es para los arrays pero creo que así me va bien
			if (is_array($value)) {
                $sub = new Usuario;
                $sub->set($value);
                $value = $sub;
            }*/
            $this->{$key} = $value;
        }
    }
	
	public $id;
    public $uid;
    public $firebaseToken;
    public $nombre;
    public $apellidos;
    public $user;
    public $email;
    public $password;
    public $nPuntos;
    public $CIF;
    public $localizacion;
    public $biografia;
    public $web;
    public $imagenPerfil;
    public $verificado;
    //Contenedores para los elementos de los que es propietario el usuario
    public $misProyectos;
    public $misAreasInteres;
    public $misEspecialidades;
    public $misSeguidos;
    public $misRedesSociales;
    public $misSolicitudes;
    public $miStatus;
}
?>