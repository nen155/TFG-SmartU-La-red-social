<?php
namespace App\Model;

use App\Lib\Database;
use App\Lib\Response;
include_once "class.notificacion.php";

class NotificacionModel
{
    private $db;
    private $table = 'notificacion';
    private $response;
    
    public function __CONSTRUCT()
    {
        $this->db = Database::StartUp();
        $this->response = new Response();
    }
    
    public function GetAll($offset,$limit)
    {
		try
		{	
			//Recojo el total de usuarios del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
            $notificaciones=array("notificaciones"=>array("notificaciones"=>array()),"totalserver"=>$totalserver["totalserver"]);
			
			$result = array();
			$stm = $this->db->prepare("SELECT c.id,c.descripcion,c.fecha,c.idUsuario,c.idProyecto,u.nombre as usuario,p.nombre as proyecto ".
			" FROM ". $this->table ." as c INNER JOIN proyecto as p ON c.idProyecto=p.id INNER JOIN usuario as u ON c.idUsuario=u.id LIMIT ".$offset.",".$limit);
			$stm->execute();


			$this->response->setResponse(true);
			
			while ($fila =$stm->fetch(\PDO::FETCH_ASSOC)){
				$notificacion = new Notificacion();
				$notificacion->set($fila);
				
				array_push($notificaciones["notificaciones"]["notificaciones"],$notificacion);
			}
			
            $this->response->result = $notificaciones;
			
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}
    }
	public function GetbyIds($ids)
    {
		
		$this->response->setResponse(true);
		//Recojo el total de notificaciones del server
		$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
		$stm->execute();
		$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
		//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
        $notificaciones=array("notificaciones"=>array("notificaciones"=>array()),"totalserver"=>$totalserver["totalserver"]);
		
		for($i=0;$i<count($ids);++$i){
			$notificacion =$this->Get($ids[$i]);
			array_push($notificaciones["notificaciones"]["notificaciones"],$notificacion["notificacion"]);
		}
		
		$this->response->result = $notificaciones;
			
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}
    }
	  public function Get($id)
    {
		try
		{	
			
			$result = array();
			$stm = $this->db->prepare("SELECT c.id,c.descripcion,c.fecha,c.idUsuario,c.idProyecto,u.nombre as usuario,p.nombre as proyecto ".
			" FROM ". $this->table ." as c INNER JOIN proyecto as p ON c.idProyecto=p.id INNER JOIN usuario as u ON c.idUsuario=u.id WHERE c.id= ? ");
			$stm->execute(array($id));


			$this->response->setResponse(true);
			
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);
			$notificacion = new Notificacion();
			$notificacion->set($fila);
				
            $this->response->result = array("notificacion"=>$notificacion);
			
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}
    }
	
	public function InsertNotificacion($data)
	{
		
		try 
		{
                $sql = "INSERT INTO notificacion
                            (id, descripcion, fecha,idUsuario,idProyecto)
                            VALUES (NULL,?,?,?,?)";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['descripcion'],
                            $data['fecha'],
							$data['idUsuario'],
							$data['idProyecto']
                        )
                    ); 
            
            
			$this->response->setResponse(true);
			$this->response->result="{\"resultado\":\"ok\"}";
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
		}
    }
}