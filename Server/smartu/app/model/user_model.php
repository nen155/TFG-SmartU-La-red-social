<?php
namespace App\Model;

use App\Lib\Database;
use App\Lib\Response;

class UserModel
{
    private $db;
    private $table = 'usuario';
    private $response;
    
    public function __CONSTRUCT()
    {
        $this->db = Database::StartUp();
        $this->response = new Response();
    }
    
    public function GetAll($offset,$limit,$id=null)
    {
		try
		{	
			//Recojo el total de usuarios del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
            $usuarios=array("usuarios"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			$result = array();
			if($id==null){
				$stm = $this->db->prepare("SELECT id,nombre,apellidos,verificado,user,email,nPuntos,biografia,web,imagenPerfil,uid,firebaseToken FROM ". $this->table ." LIMIT ".$offset.",".$limit);
				$stm->execute();
			}
			else
			{
				$stm = $this->db->prepare("SELECT u.id,u.nombre,u.apellidos,u.verificado,u.user,u.email,u.nPuntos,u.biografia,u.web,u.imagenPerfil,u.uid,u.firebaseToken".
				" FROM ". $this->table . " as u INNER JOIN usuarioColaboradorProyecto as c ON u.id=c.idUsuario". 
				" WHERE c.idProyecto= ?".
				" LIMIT ".$offset.",".$limit);
				$stm->execute(array($id));
			}
			$this->response->setResponse(true);
			
			while ($fila =$stm->fetch(\PDO::FETCH_ASSOC)){
				$usuario = new \Usuario();
				$usuario->set($fila);
				
				//Recojo misProyectos
				$stmSub = $this->db->prepare("SELECT idProyecto FROM usuarioColaboradorProyecto WHERE idUsuario= ?");
				$stmSub->execute(array($usuario->id));
				$usuario->misProyectos=$stmSub->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo misAreasInteres
				$stmSub = $this->db->prepare("SELECT a.id,a.nombre,a.descripcion,m.url as urlImg FROM usuarioInteresaArea as u".
				" INNER JOIN area as a ON u.idArea=a.id LEFT JOIN multimedia as m ON a.idImagenDestacada=m.id WHERE u.idUsuario= ?");
				$stmSub->execute(array($usuario->id));
				$usuario->misAreasInteres=array();
				while ($filaA =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misAreasInteres,$filaA);
				
				//Recojo misEspecialidades
				$stmSub = $this->db->prepare("SELECT e.id,e.nombre,e.descripcion,u.experiencia FROM usuarioEspecialidad as u".
				" INNER JOIN especialidad as e ON u.idEspecialidad=e.id WHERE u.idUsuario= ?");
				$stmSub->execute(array($usuario->id));
				// array(array()) resultado misEspecialidades:[{..},{..},..]
				$usuario->misEspecialidades=array();
				while ($filaE =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misEspecialidades,$filaE);
				
				//Recojo misSeguidos
				$stmSub = $this->db->prepare("SELECT idUsuarioSeguido FROM seguidor WHERE idUsuario= ?");
				$stmSub->execute(array($usuario->id));
				$usuario->misSeguidos=$stmSub->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo misRedesSociales
				$stmSub = $this->db->prepare("SELECT r.id,r.nombre,r.url FROM redSocial as r WHERE r.idUsuario= ?");
				$stmSub->execute(array($usuario->id));
				// array(array()) resultado misRedesSociales:[{..},{..},..]
				$usuario->misRedesSociales=array();
				while ($filaR =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misRedesSociales,$filaR);
				
				//Recojo misSolicitudes
				$stmSub = $this->db->prepare("SELECT s.id,s.fecha,s.descripcion,s.idProyecto,p.nombre FROM solicitudUnion as s INNER JOIN proyecto as p ON s.idProyecto=p.id WHERE s.idUsuarioSolicitante= ?");
				$stmSub->execute(array($usuario->id));
				// array(array()) resultado misSolicitudes:[{..},{..},..]
				$usuario->misSolicitudes=array();
				while ($filaS =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misSolicitudes,$filaS);

				
				//Recojo miStatus
				$stmSub = $this->db->prepare("SELECT s.id,s.nombre,s.puntos, (SELECT count(*) FROM seguidor as e WHERE e.idUsuarioSeguido=?) as numSeguidores".
				" FROM status as s INNER JOIN usuario as u ON u.idStatus=s.id WHERE u.id= ?");
				$stmSub->execute(array($usuario->id,$usuario->id));
				// array(array()) resultado miStatus:[{..},{..},..]
				$filaSt =$stmSub->fetch(\PDO::FETCH_ASSOC);
				$usuario->miStatus=$filaSt;

				
				array_push($usuarios["usuarios"],$usuario);
			}
			
            $this->response->result = $usuarios;
			
            
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
			//Recojo el total de usuarios del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
			$usuarios=array("usuarios"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			for($i=0;$i<count($ids);++$i){
				$usuario =$this->Get($ids[$i]);
				array_push($usuarios["usuarios"],$usuario["usuario"]);
			}
			
			$this->response->result = $usuarios;
			
            
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

			$stm = $this->db->prepare("SELECT id,nombre,apellidos,verificado,user,email,nPuntos,biografia,web,imagenPerfil,uid,firebaseToken FROM ". $this->table ." WHERE id= ?");
			$stm->execute();

			$stm->execute(array($id));
			
			$this->response->setResponse(true);
			
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);
				$usuario = new \Usuario();
				$usuario->set($fila);
				
				//Recojo misProyectos
				$stm = $this->db->prepare("SELECT idProyecto FROM usuarioColaboradorProyecto WHERE idUsuario= ?");
				$stm->execute(array($usuario->id));
				$usuario->misProyectos=$stm->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo misAreasInteres
				$stm = $this->db->prepare("SELECT a.id,a.nombre,a.descripcion,m.url as urlImg FROM usuarioInteresaArea as u".
				" INNER JOIN area as a ON u.idArea=a.id LEFT JOIN multimedia as m ON a.idImagenDestacada=m.id WHERE u.idUsuario= ?");
				$stm->execute(array($usuario->id));
				$usuario->misAreasInteres=array();
				while ($filaA =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misAreasInteres,$filaA);
				
				//Recojo misEspecialidades
				$stm = $this->db->prepare("SELECT e.id,e.nombre,e.descripcion,u.experiencia FROM usuarioEspecialidad as u".
				" INNER JOIN especialidad as e ON u.idEspecialidad=e.id WHERE u.idUsuario= ?");
				$stm->execute(array($usuario->id));
				// array(array()) resultado misEspecialidades:[{..},{..},..]
				$usuario->misEspecialidades=array();
				while ($filaE =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misEspecialidades,$filaE);
				
				//Recojo misSeguidos
				$stm = $this->db->prepare("SELECT idUsuarioSeguido FROM seguidor WHERE idUsuario= ?");
				$stm->execute(array($usuario->id));
				$usuario->misSeguidos=$stm->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo misRedesSociales
				$stm = $this->db->prepare("SELECT r.id,r.nombre,r.url FROM redSocial as r WHERE r.idUsuario= ?");
				$stm->execute(array($usuario->id));
				// array(array()) resultado misRedesSociales:[{..},{..},..]
				$usuario->misRedesSociales=array();
				while ($filaR =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misRedesSociales,$filaR);
				
				//Recojo misSolicitudes
				$stm = $this->db->prepare("SELECT s.id,s.fecha,s.descripcion,s.idProyecto,p.nombre FROM solicitudUnion as s INNER JOIN proyecto as p ON s.idProyecto=p.id WHERE s.idUsuarioSolicitante= ?");
				$stm->execute(array($usuario->id));
				// array(array()) resultado misSolicitudes:[{..},{..},..]
				$usuario->misSolicitudes=array();
				while ($filaS =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misSolicitudes,$filaS);

				
				//Recojo miStatus
				$stm = $this->db->prepare("SELECT s.id,s.nombre,s.puntos, (SELECT count(*) FROM seguidor as e WHERE e.idUsuarioSeguido=?) as numSeguidores".
				" FROM status as s INNER JOIN usuario as u ON u.idStatus=s.id WHERE u.id= ?");
				$stm->execute(array($usuario->id,$usuario->id));
				// array(array()) resultado miStatus:[{..},{..},..]
				$filaSt =$stm->fetch(\PDO::FETCH_ASSOC);
				$usuario->miStatus=$filaSt;
			
            $this->response->result = array("usuario"=>$usuario);
			
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}
    }
	
	
    public function Login($email,$password)
    {
		try
		{
			$stm = $this->db->prepare("SELECT id,nombre,apellidos,verificado,user,email,nPuntos,biografia,web,imagenPerfil,uid,firebaseToken FROM " .$this->table ." WHERE email=? AND password=?");
			$stm->execute(array($email,$password));

			$this->response->setResponse(true);
			if($stm->rowCount()>0)
			{
				$fila =$stm->fetch(\PDO::FETCH_ASSOC);
				$usuario = new \Usuario();
				$usuario->set($fila);
				
				//Recojo misProyectos
				$stm = $this->db->prepare("SELECT idProyecto FROM usuarioColaboradorProyecto WHERE idUsuario= ?");
				$stm->execute(array($usuario->id));
				$usuario->misProyectos=$stm->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo misAreasInteres
				$stm = $this->db->prepare("SELECT a.id,a.nombre,a.descripcion,m.url as urlImg FROM usuarioInteresaArea as u".
				" INNER JOIN area as a ON u.idArea=a.id LEFT JOIN multimedia as m ON a.idImagenDestacada=m.id WHERE u.idUsuario= ?");
				$stm->execute(array($usuario->id));
				$usuario->misAreasInteres=array();
				while ($filaA =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misAreasInteres,$filaA);
				
				//Recojo misEspecialidades
				$stm = $this->db->prepare("SELECT e.id,e.nombre,e.descripcion,u.experiencia FROM usuarioEspecialidad as u".
				" INNER JOIN especialidad as e ON u.idEspecialidad=e.id WHERE u.idUsuario= ?");
				$stm->execute(array($usuario->id));
				// array(array()) resultado misEspecialidades:[{..},{..},..]
				$usuario->misEspecialidades=array();
				while ($filaE =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misEspecialidades,$filaE);
				
				//Recojo misSeguidos
				$stm = $this->db->prepare("SELECT idUsuarioSeguido FROM seguidor WHERE idUsuario= ?");
				$stm->execute(array($usuario->id));
				$usuario->misSeguidos=$stm->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo misRedesSociales
				$stm = $this->db->prepare("SELECT r.id,r.nombre,r.url FROM redSocial as r WHERE r.idUsuario= ?");
				$stm->execute(array($usuario->id));
				// array(array()) resultado misRedesSociales:[{..},{..},..]
				$usuario->misRedesSociales=array();
				while ($filaR =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misRedesSociales,$filaR);
				
				//Recojo misSolicitudes
				$stm = $this->db->prepare("SELECT s.id,s.fecha,s.descripcion,s.idProyecto,p.nombre FROM solicitudUnion as s INNER JOIN proyecto as p ON s.idProyecto=p.id WHERE s.idUsuarioSolicitante= ?");
				$stm->execute(array($usuario->id));
				// array(array()) resultado misSolicitudes:[{..},{..},..]
				$usuario->misSolicitudes=array();
				while ($filaS =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($usuario->misSolicitudes,$filaS);

				
				//Recojo miStatus
				$stm = $this->db->prepare("SELECT s.id,s.nombre,s.puntos, (SELECT count(*) FROM seguidor as e WHERE e.idUsuarioSeguido=?) as numSeguidores".
				" FROM status as s INNER JOIN usuario as u ON u.idStatus=s.id WHERE u.id= ?");
				$stm->execute(array($usuario->id,$usuario->id));
				// array(array()) resultado miStatus:[{..},{..},..]
				$filaSt =$stm->fetch(\PDO::FETCH_ASSOC);
				$usuario->miStatus=$filaSt;
			
            $this->response->result = array("usuario"=>$usuario);
				
				
			}
			else
				$this->response->result = null;
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}  
    }
    
    public function InsertOrUpdate($data)
    {
		
		try 
		{
            if(isset($data['id']) && $data['id']!=-1)
            {
				///TODO Se realizaría el UPDATE pero como en la app no se ha
				///creado esta posibilidad lo dejo por hacer
            }
            else
            {
				//TODO Se debe implementar la función de verificado en funcion de si el email
				//procede de alguna institución como la UGR o similar, en tal caso sería verificado
				//Se deja planteado para hacer
                $sql = "INSERT INTO $this->table
                            (nombre, apellidos, email, password, user, uid, firebaseToken)
                            VALUES (?,?,?,?,?,?,?)";
                
                $smt= $this->db->prepare($sql);
                 $smt->execute(
                        array(
                            $data['nombre'], 
                            $data['apellidos'],
                            $data['email'],
                            $data['password'],
                            $data['user'],
                            $data['uid'],
                            $data['firebaseToken']
                        )
                    ); 
            }
            
			$this->response->setResponse(true);
			
			$this->response->result= array("resultado" =>"ok" );
			$data["idUsuario"]=$smt->lastInsertId();
			//Inserto notificacion
			$this->InsertaNotificacionUsuario($data);
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
		}
    }

	
	public function InsertSeguidor($data)
	{
		
		try 
		{
			$sql = "SELECT idUsuario FROM seguidor WHERE idUsuario=? AND idUsuarioSeguido=?";
               
            $stm = $this->db->prepare($sql);
			$stm->execute(
                        array(
                            $data['idUsuario'],
                            $data['idUsuarioSeguido']
                        )
             ); 
				//Sino la he insertado antes
			if($stm->rowCount()==0){
                $sql = "INSERT INTO seguidor
                            (id, idUsuario, idUsuarioSeguido)
                            VALUES (NULL,?,?)";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idUsuarioSeguido']
                        )
                    ); 
			}
					
            $datos = array("idUsuarioSeguido"=>$data['idUsuarioSeguido'],"tipo"=>"seguidor","operacion"=>"plus");
            //Actualizo los puntos del propietario del proyecto
			if($stm->rowCount()==0)
				$this->UpdatePuntosUsuario($datos);
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			//Inserto notificacion
			if($stm->rowCount()==0)
				$this->InsertaNotificacionSeguir($data);
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	
	public function DeleteSeguidor($data)
	{
		
		try 
		{
                $sql = "DELETE FROM seguidor WHERE idUsuario=? AND idUsuarioSeguido=?";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idUsuarioSeguido']
                        )
                    ); 
            $datos = array("idUsuarioSeguido"=>$data['idUsuarioSeguido'],"tipo"=>"seguidor","operacion"=>"minus");
            //Actualizo los puntos del propietario del proyecto
            $this->UpdatePuntosUsuario($datos);
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	public function InsertBuenaIdea($data)
	{
		
		try 
		{
                $sql = "INSERT INTO buenaIdea
                            (id, idUsuario, idProyecto)
                            VALUES (NULL,?,?)";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idProyecto']
                        )
                    ); 
					
			$datos = array("idUsuario"=>$data['idUsuario'],"idProyecto"=>$data['idProyecto'],"tipo"=>"idea","operacion"=>"plus");
            //Actualizo los puntos del propietario del proyecto
            $this->UpdatePuntosUsuario($datos);
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
			$this->InsertaNotificacionBuenaIdea($data);
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	
	public function DeleteBuenaIdea($data)
	{
		
		try 
		{
                $sql = "DELETE FROM buenaIdea WHERE idUsuario=? AND idProyecto=?";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idProyecto']
                        )
                    ); 
            $datos = array("idUsuario"=>$data['idUsuario'],"idProyecto"=>$data['idProyecto'],"tipo"=>"idea","operacion"=>"minus");
			
            //Actualizo los puntos del propietario del proyecto
            $this->UpdatePuntosUsuario($datos);
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	
	public function InsertSolicitudUnion($data)
	{
		
		try 
		{
			$sql = "SELECT idUsuarioSolicitante as idUsuario,idProyecto FROM solicitudUnion WHERE idUsuarioSolicitante=? AND idProyecto=?";
               
            $stm = $this->db->prepare($sql);
			$stm->execute(
                        array(
                            $data['idUsuario'],
                            $data['idProyecto']
                        )
             ); 
				//Sino la he insertado antes
			if($stm->rowCount()==0){
					$sql = "INSERT INTO solicitudUnion
								(id, idUsuarioSolicitante, idProyecto,fecha,descripcion)
								VALUES (NULL,?,?,?,?)";
					
					$this->db->prepare($sql)
						 ->execute(
							array(
								$data['idUsuario'],
								$data['idProyecto'],
								date("Y-m-d H:i:s"),
								$data['descripcion']
							)
						); 
			}
			
			$datos = array("idUsuario"=>$data['idUsuario'],"idProyecto"=>$data['idProyecto'],"tipo"=>"union","operacion"=>"plus");
			
            //Actualizo los puntos del propietario del proyecto
			if($stm->rowCount()==0)
				$this->UpdatePuntosUsuario($datos);
			
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			//Inserto notificacion
			if($stm->rowCount()==0)
				$this->InsertaNotificacionSolicitud($data);
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	
	public function DeleteSolicitudUnion($data)
	{
		
		try 
		{
                $sql = "DELETE FROM solicitudUnion WHERE idUsuarioSolicitante=? AND idProyecto=?";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idProyecto']
                        )
                    ); 
			$datos = array("idUsuario"=>$data['idUsuario'],"idProyecto"=>$data['idProyecto'],"tipo"=>"union","operacion"=>"minus");

            //Actualizo los puntos del propietario del proyecto
            $this->UpdatePuntosUsuario($datos );
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	
	public function InsertAreasInteres($data)
	{
		
		try 
		{
			for($i=0;$i<count($data["idsAreas"]);++$i){
				$sql = "SELECT idUsuario,idProyecto FROM usuarioInteresaArea WHERE idUsuario=? AND idArea=?";
               
                 $stm = $this->db->prepare($sql);
				 $stm->execute(
                        array(
                            $data['idUsuario'],
                            $data['idsAreas'][$i]
                        )
                    );
				
				if($stm->rowCount()==0){
					$sql = "INSERT INTO usuarioInteresaArea
								(id, idUsuario, idArea)
								VALUES (NULL,?,?)";
					
					$this->db->prepare($sql)
						 ->execute(
							array(
								$data['idUsuario'],
								$data['idsAreas'][$i]
							)
						); 
				}
            }
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			//Inserto notificacion
			if($stm->rowCount()==0)
				$this->InsertaNotificacionInteres($data);
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	public function UpdatePuntosUsuario($data)
	{
		
		try 
		{
			//Recojo el ID del usuario
			if(isset($data["idUsuarioSeguido"])){
				$id=$data["idUsuarioSeguido"];
			}else{
				//Sólo le doy puntos al creador del proyecto,
				//se puede implementar para los colaboradores si se quiere
				//sería hacer un for por cada uno de los colaboradores e ir añadiendole puntos a cada
				//uno y comprobar el status de cada uno
				$sql = "SELECT idUsuario FROM proyecto WHERE id=?";
				$stm = $this->db->prepare($sql);
				$stm->execute(array($data["idProyecto"]));
				$filaA =$stm->fetch(\PDO::FETCH_ASSOC);
				$id=$filaA["idUsuario"];
			}

			//Obtengo los puntos y el status del usuario
			$sql = "SELECT u.id as idUsuario,u.nombre,u.nPuntos,u.idStatus,s.nombre as estatus FROM usuario as u LEFT JOIN status as s ON u.idStatus=s.id  WHERE u.id=?";
			$stm = $this->db->prepare($sql);
			$stm->execute(array($id));
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);

			
			//Lógica de la gamificación
			//Depenciendo del tipo sumaré mas o menos puntos
			switch($data["tipo"]){
				case "union":
					if($data["operacion"]=="plus")
						$nPuntos = $fila["nPuntos"]+3;
					else
						$nPuntos = $fila["nPuntos"]-3;
				break;
				case "seguidor":
					if($data["operacion"]=="plus")
						$nPuntos = $fila["nPuntos"]+1;
					else
						$nPuntos = $fila["nPuntos"]-1;
				break;
				case "idea":
					if($data["operacion"]=="plus")
						$nPuntos = $fila["nPuntos"]+2;
					else
						$nPuntos = $fila["nPuntos"]-2;
				break;
			}
			
			if($nPuntos<=10){
				$idStatus=1;
			}
			if($nPuntos>10){
				$idStatus=2;
			}
			if($nPuntos>30){
				$idStatus=3;
			}
			if($nPuntos>40){
				$idStatus=4;
			}
			
			//Actualizo puntos y estado de usuario
			$sql = "UPDATE usuario SET nPuntos =?, idStatus=? WHERE id=?";
			$stm = $this->db->prepare($sql);
			$stm->execute(array($nPuntos,$idStatus,$id));
			
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
			//Muestro la notificación si ha subido de status
			if($idStatus!=$fila["idStatus"]){
				$this->InsertaNotificacionStatus($fila);
			}
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	
	
	/*
	*
	*INSERCCIÓN DE NOTIFICACIONES 
	*
	*/
	public function InsertaNotificacionStatus($data){
			
			$datos=array("nombre"=>"El usuario ".$data["nombre"]." ha subido de status!",
			"descripcion"=>"El usuario ".$data["nombre"]." ahora es ".$data["estatus"],
			 "idUsuario"=>$data['idUsuario'],
			 0);
			 
			$pub = new NotificacionModel();
			$pub->InsertNotificacion($datos);
	}
	
	public function InsertaNotificacionUsuario($data){
			
			$datos=array("nombre"=>"Nuevo usuario en la red!",
			"descripcion"=>"El usuario ".$data["nombre"]." ahora está en SmartU",
			 "idUsuario"=>$data['idUsuario'],
			 0);
			 
			$pub = new NotificacionModel();
			$pub->InsertNotificacion($datos);
	}
	public function InsertaNotificacionSeguir($data){
			
			//Busco el proyecto
			$stm = $this->db->prepare("SELECT nombre as seguido FROM usuario WHERE id=?");
			$stm->execute(array($data["idUsuarioSeguido"]));
			$seguido = $stm->fetch(\PDO::FETCH_ASSOC);
			
			//Busco el usuario
			$stm = $this->db->prepare("SELECT nombre as usuario FROM  usuario WHERE id=?");
			$stm->execute(array($data["idUsuario"]));
			$usuario = $stm->fetch(\PDO::FETCH_ASSOC);
			
			$datos=array("nombre"=>"El usuario ".$usuario["usuario"]." ha seguido a alguien!",
			"descripcion"=>"El usuario ".$usuario["usuario"]." ahora está siguiendo a ".$seguido["seguido"],
			 "idUsuario"=>$data['idUsuario'],
			 0);
			 
			$pub = new NotificacionModel();
			$pub->InsertNotificacion($datos);
	}
	
	public function InsertaNotificacionBuenaIdea($data){
			
			//Busco el proyecto
			$stm = $this->db->prepare("SELECT nombre as proyecto FROM  proyecto WHERE id=?");
			$stm->execute(array($data["idProyecto"]));
			$proyecto = $stm->fetch(\PDO::FETCH_ASSOC);
			
			//Busco el usuario
			$stm = $this->db->prepare("SELECT nombre as usuario FROM  usuario WHERE id=?");
			$stm->execute(array($data["idUsuario"]));
			$usuario = $stm->fetch(\PDO::FETCH_ASSOC);
			
			$datos=array("nombre"=>"El proyecto ".$proyecto["proyecto"]." es una buena idea!",
			"descripcion"=>"El usuario ".$usuario["usuario"]." ha dado una buena idea al proyecto ".$proyecto["proyecto"],
			 "idUsuario"=>$data['idUsuario'],
			 "idProyecto"=>$data['idProyecto']);
			 
			$pub = new NotificacionModel();
			$pub->InsertNotificacion($datos);
	}
	
	
	
	public function InsertaNotificacionSolicitud($data){
			
			//Busco el proyecto
			$stm = $this->db->prepare("SELECT nombre as proyecto FROM  proyecto WHERE id=?");
			$stm->execute(array($data["idProyecto"]));
			$proyecto = $stm->fetch(\PDO::FETCH_ASSOC);
			
			//Busco el usuario
			$stm = $this->db->prepare("SELECT nombre as usuario FROM  usuario WHERE id=?");
			$stm->execute(array($data["idUsuario"]));
			$usuario = $stm->fetch(\PDO::FETCH_ASSOC);
			
			$datos=array("nombre"=>"Nueva solicitud de unión en ".$proyecto["proyecto"]."!",
			"descripcion"=>"El usuario ".$usuario["usuario"]." quiere unirse al proyecto ",
			 "idUsuario"=>$data['idUsuario'],
			 "idProyecto"=>$data['idProyecto']);
			 
			$pub = new NotificacionModel();
			$pub->InsertNotificacion($datos);
	}
	
	public function InsertaNotificacionInteres($data){
			
			//Busco el areas
			for($i=0;$i<count($data["idsAreas"]);++$i){
				$stm = $this->db->prepare("SELECT nombre as interes FROM area WHERE id=?");
				$stm->execute(array($data['idsAreas'][$i]));
				$area=$stm->fetch(\PDO::FETCH_ASSOC);
				if($i!=count($data["idsAreas"])-1)
					$areas.="".$area["interes"].", ";
				else
					$areas.="".$area["interes"];
			}
			
			//Busco el usuario
			$stm = $this->db->prepare("SELECT nombre as usuario FROM  usuario WHERE id=?");
			$stm->execute(array($data["idUsuario"]));
			$usuario = $stm->fetch(\PDO::FETCH_ASSOC);
			
			$datos=array("nombre"=>"El usuario ". $usuario["usuario"] . " tiene nuevos intereses!",
			"descripcion"=>"Al usuario ".$usuario["usuario"]." ahora le interesan ".$areas,
			 "idUsuario"=>$data['idUsuario'],
			 0);
			 
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