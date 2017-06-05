<?php
namespace App\Model;

use App\Lib\Database;
use App\Lib\Response;
include_once "class.usuario.php";

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
				$stmSub = $this->db->prepare("SELECT s.id,s.nombre,s.puntos, (SELECT count(*) FROM seguidor as e WHERE e.idUsuario=?) as numSeguidores".
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
		
			$result = array();

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
				$stm = $this->db->prepare("SELECT e.id,e.nombre,e.descripcion,e.experiencia FROM usuarioEspecialidad as u".
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
				$stm = $this->db->prepare("SELECT s.id,s.nombre,s.puntos, (SELECT count(*) FROM seguidor as e WHERE e.idUsuario=?) as numSeguidores FROM status as s WHERE s.idUsuario= ?");
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
			$result = array();

			$stm = $this->db->prepare("SELECT id,nombre,apellidos,verificado,user,email,nPuntos,biografia,web,imagenPerfil,uid,firebaseToken FROM $this->table WHERE email = ? AND password= ?");
			$stm->execute(array($email,$password));

			$this->response->setResponse(true);
			if($stm->rowCount>0)
				$this->response->result = $stm->fetch();
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
                
                $this->db->prepare($sql)
                     ->execute(
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
            
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
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
            
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
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
            
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
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
            
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
		}
    }
	
	public function InsertSolicitudUnion($data)
	{
		
		try 
		{
                $sql = "INSERT INTO solicitudUnion
                            (id, idUsuario, idProyecto,fecha,descripcion)
                            VALUES (NULL,?,?,?,?)";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idProyecto'],
							$data['fecha'],
							$data['descripcion']
                        )
                    ); 
            
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
		}
    }
	
	public function DeleteSolicitudUnion($data)
	{
		
		try 
		{
                $sql = "DELETE FROM solicitudUnion WHERE idUsuario=? AND idProyecto=?";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idProyecto']
                        )
                    ); 
            
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
		}
    }
	
	public function InsertAreasInteres($data)
	{
		
		try 
		{
			for($i=0;$i<count($data["idsAreas"]);++$i){
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
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
		}
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