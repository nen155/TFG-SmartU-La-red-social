<?php
class Proyecto{
	//Si tengo el paramétro json significa que lo quiero convertir de un json
	 public function __construct($json = false) {
        if ($json) $this->set(json_decode($json, true));
    }
	//Convierte del JSON a el objeto que tengo
    public function set($data) {
        foreach ($data AS $key => $value) {
             /*Se supon que es para los arrays pero creo que así me va bien
			if (is_array($value)) {
                $sub = new Proyecto;
                $sub->set($value);
                $value = $sub;
            }*/
            $this->{$key} = $value;
        }
    }
	
	public $id;
    public $nombre;
	public $descripcion;
    public $fechaCreacion;
    public $fechaFinalizacion;
    public $imagenDestacada;
    public $localizacion;
    public $coordenadas;
    public $web;
    public $idPropietario;
    public $propietarioUser;
    //Contenedores de los que es idPropietario
    public $buenaIdea;
    public $misAreas;
    public $vacantesProyecto;
    public $misArchivos;
    public $misRedesSociales;
    public $misHashtag;
    public $misAvances;
    public $integrantes;

}
?>