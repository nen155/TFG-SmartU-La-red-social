<?php
namespace App\Model;

use App\Lib\Database;
use App\Lib\Response;

class ComentarioModel
{
    private $db;
    private $table = 'comentario';
    private $response;
    
    public function __CONSTRUCT()
    {
        $this->db = Database::StartUp();
        $this->response = new Response();
    }
    
    public function GetAll($offset=0,$limit=10,$id=null)
    {
		try
		{	
			//Recojo el total de usuarios del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
            $comentarios=array("comentarios"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			$result = array();
			if($id==null){
				$stm = $this->db->prepare("SELECT c.id,c.descripcion,c.fecha,c.idUsuario,c.idProyecto,u.nombre as usuario,p.nombre as proyecto ".
				" FROM ". $this->table ." as c INNER JOIN proyecto as p ON c.idProyecto=p.id INNER JOIN usuario as u ON c.idUsuario=u.id ORDER BY c.fecha DESC LIMIT ".$offset.",".$limit);
				$stm->execute();
			}
			else
			{
				$stm = $this->db->prepare("SELECT c.id,c.descripcion,c.fecha,c.idUsuario,c.idProyecto,u.nombre as usuario,p.nombre as proyecto ".
				" FROM ". $this->table ." as c INNER JOIN proyecto as p ON c.idProyecto=p.id INNER JOIN usuario as u ON c.idUsuario=u.id".
				" WHERE c.idProyecto= ?".
				"ORDER BY c.fecha DESC LIMIT ".$offset.",".$limit);
				$stm->execute(array($id));
			}
			$this->response->setResponse(true);
			
			while ($fila =$stm->fetch(\PDO::FETCH_ASSOC)){
				$comentario = new \Comentario();
				$comentario->set($fila);
				
				array_push($comentarios["comentarios"],$comentario);
			}
			
            $this->response->result = $comentarios;
			
            
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
		try{
			
			$this->response->setResponse(true);
			//Recojo el total de comentarios del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
			$comentarios=array("comentarios"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			for($i=0;$i<count($ids);++$i){
				$comentario =$this->Get($ids[$i]);
				array_push($comentarios["comentarios"],$comentario["comentario"]);
			}
			
			$this->response->result = $comentarios;
			
            
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
			
			
				$stm = $this->db->prepare("SELECT c.id,c.descripcion,c.fecha,c.idUsuario,c.idProyecto,u.nombre as usuario,p.nombre as proyecto ".
				" FROM ". $this->table ." as c INNER JOIN proyecto as p ON c.idProyecto=p.id INNER JOIN usuario as u ON c.idUsuario=u.id WHERE c.id=?");
				$stm->execute(array($id));
		
			$this->response->setResponse(true);
			
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);
			$comentario = new \Comentario();
			$comentario->set($fila);
				
            $this->response->result = array("comentario"=>$comentario);
			
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}
    }
	
	public function InsertComentario($data)
	{
		
		try 
		{
                $sql = "INSERT INTO comentario
                            (id, descripcion, fecha,idUsuario,idProyecto)
                            VALUES (NULL,?,?,?,?)";
				//Conversión de fecha
                $milliseconds =  $data['fecha'];
				$timestamp = $milliseconds/1000;
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['descripcion'],
                            date("Y-m-d H:i:s", $timestamp),
							$data['idUsuario'],
							$data['idProyecto']
                        )
                    ); 
            
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			//Inserto la notificacion del comentario
			$this->InsertaNotificacionComentario($data);
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
		}
    }
	public function InsertaNotificacionComentario($data){
			//Busco el proyecto
			$stm = $this->db->prepare("SELECT nombre as proyecto FROM  proyecto WHERE id=?");
			$stm->execute(array($data["idProyecto"]));
			$proyecto = $stm->fetch(\PDO::FETCH_ASSOC);
			//Busco el usuario
			$stm = $this->db->prepare("SELECT nombre as usuario FROM  usuario WHERE id=?");
			$stm->execute(array($data["idUsuario"]));
			$usuario = $stm->fetch(\PDO::FETCH_ASSOC);
			
			if(strlen($data["descripcion"])>150){
					$descripcion=substr($data["descripcion"],0,150);
			}else
				$descripcion=$data["descripcion"];
			
			
			$datos = array("nombre"=>"Nuevo comentario en ".$proyecto["proyecto"]."!",
			"descripcion"=>"El usuario ".$usuario["usuario"]." ha hecho el siguiente comentario ".$descripcion."...",
			 "idUsuario"=>$data['idUsuario'],
			 "idProyecto"=>$data['idProyecto'],
			 "tipo"=>"comentario",
			 "accion"=>"insert");
			 
			$pub = new NotificacionModel();
			$pub->InsertNotificacion($datos);
	}
	
    /**
	* TODO Como esta función no se va a usar en la app se deja por hacer
	*/
    public function Delete($id)
    {
		try 
		{
			$stm = $this->db
			            ->prepare("DELETE FROM $this->table WHERE id = ?");			          

			$stm->execute(array($id));
            
			$this->response->setResponse(true);
            return $this->response;
		} catch (Exception $e) 
		{
			$this->response->setResponse(false, $e->getMessage());
		}
    }
}