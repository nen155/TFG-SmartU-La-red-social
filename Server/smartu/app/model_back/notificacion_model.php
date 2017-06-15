<?php
namespace App\Model;

use App\Lib\Database;
use App\Lib\Response;

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
            $notificaciones=array("notificaciones"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			$result = array();
			$stm = $this->db->prepare("SELECT c.id,c.nombre,c.descripcion,c.fecha,c.idUsuario,c.idProyecto,u.nombre as usuario,p.nombre as proyecto ".
			" FROM ". $this->table ." as c LEFT JOIN proyecto as p ON c.idProyecto=p.id LEFT JOIN usuario as u ON c.idUsuario=u.id ORDER BY c.fecha DESC LIMIT ".$offset.",".$limit);
			$stm->execute();


			$this->response->setResponse(true);
			
			while ($fila =$stm->fetch(\PDO::FETCH_ASSOC)){
				$notificacion = new \Notificacion();
				$notificacion->set($fila);
				
				array_push($notificaciones["notificaciones"],$notificacion);
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
		try{
			$this->response->setResponse(true);
			//Recojo el total de notificaciones del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
			$notificaciones=array("notificaciones"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			for($i=0;$i<count($ids);++$i){
				$notificacion =$this->Get($ids[$i]);
				array_push($notificaciones["notificaciones"],$notificacion["notificacion"]);
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
			$stm = $this->db->prepare("SELECT c.id,c.nombre,c.descripcion,c.fecha,c.idUsuario,c.idProyecto,u.nombre as usuario,p.nombre as proyecto ".
			" FROM ". $this->table ." as c LEFT JOIN proyecto as p ON c.idProyecto=p.id LEFT JOIN usuario as u ON c.idUsuario=u.id WHERE c.id= ? ");
			$stm->execute(array($id));


			$this->response->setResponse(true);
			
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);
			$notificacion = new \Notificacion();
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
                            (id,nombre, descripcion, fecha,idUsuario,idProyecto)
                            VALUES (NULL,?,?,?,?,?)";
				
                $this->db->prepare($sql)
                     ->execute(
                        array(
							$data['nombre'],
                            $data['descripcion'],
                            date("Y-m-d H:i:s"),
							$data['idUsuario'],
							$data['idProyecto']
                        )
                    ); 
            
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
			$datos =array(
							"id"=>$this->db->lastInsertId(),
							"nombre"=>$data['nombre'],
                            "descripcion"=>$data['descripcion'],
                            "fecha"=>date("Y-m-d H:i:s"),
							"idUsuario"=>$data['idUsuario'],
							"idProyecto"=>$data['idProyecto']
                        );
			//Envío notificación a FCM
			$this->send_notification($datos);
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
		}
    }
	//Envia notificación a FCM
	public function send_notification ($data)
	{
		
		//Busco el proyecto
		$stm = $this->db->prepare("SELECT nombre as proyecto FROM  proyecto WHERE id=?");
		$stm->execute(array($data["idProyecto"]));
		$proyecto = $stm->fetch(\PDO::FETCH_ASSOC);
		//Busco el usuario
		$stm = $this->db->prepare("SELECT nombre as usuario FROM  usuario WHERE id=?");
		$stm->execute(array($data["idUsuario"]));
		$usuario = $stm->fetch(\PDO::FETCH_ASSOC);
			
		$path_to_firebase_cm = 'https://fcm.googleapis.com/fcm/send';
		$token="/topics/notificaciones";
		$fields = array(
            'to' => $token,
            'notification' => array('title' => $data['nombre'], 'body' => $data['descripcion']),
            'data' =>  array("id"=>$data['id'],
							"nombre"=>$data['nombre'],
                            "descripcion"=>$data['descripcion'],
                            "fecha"=>date("Y-m-d H:i:s"),
							"idUsuario"=>$data['idUsuario'],
							"idProyecto"=>$data['idProyecto'],
							"usuario"=>$usuario["usuario"],
							"proyecto"=>$proyecto["proyecto"]
                        )
        );
 
        $headers = array(
            'Authorization:key=AAAAuJ1GZyo:APA91bGBJIf5D_YLdMHPev4IwWfpcsosMKTogoYedzEkDSWUBBFX2uDziS25okqCueSy6CdmftHwjl4C0-mfhqQUXdEAA4an7Q2kKK2sYkoeqiI2I9X9_Csy_n-_5t3Xb1EKIl6Fuu8Y',
            'Content-Type:application/json'
        );		
		$ch = curl_init();
 
        curl_setopt($ch, CURLOPT_URL, $path_to_firebase_cm); 
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true); 
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_IPRESOLVE, CURL_IPRESOLVE_V4 ); 
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
    
        $result = curl_exec($ch);
       
	}	
	
	
	
}