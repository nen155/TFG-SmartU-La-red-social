<?php
namespace App\Model;

use App\Lib\Database;
use App\Lib\Response;

class AreaModel
{
    private $db;
    private $table = 'area';
    private $response;
    
    public function __CONSTRUCT()
    {
        $this->db = Database::StartUp();
        $this->response = new Response();
    }
    
    public function GetAll()
    {
		try
		{	
			//Recojo el total de usuarios del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
            $areas=array("areas"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			$result = array();
			$stm = $this->db->prepare("SELECT a.id,a.nombre,a.descripcion, m.url as urlImg FROM ". $this->table." as a LEFT JOIN multimedia as m ON a.idImagenDestacada=m.id");
			$stm->execute();
			
			$this->response->setResponse(true);
			
			while ($fila =$stm->fetch(\PDO::FETCH_ASSOC)){
				array_push($areas["areas"],$fila);
			}
			
            $this->response->result = $areas;
			
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}
    }
}