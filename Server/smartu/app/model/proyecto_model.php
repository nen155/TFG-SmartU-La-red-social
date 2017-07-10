<?php
namespace App\Model;

use App\Lib\Database;
use App\Lib\Response;

class ProyectoModel
{
    private $db;
    private $table = 'proyecto';
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
            $proyectos=array("proyectos"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			if($id==null){
				$stm = $this->db->prepare("SELECT p.id,p.nombre,p.descripcion,p.fechaCreacion,p.fechaFinalizacion,m.url as imagenDestacada,p.localizacion,p.coordenadas,p.web,p.idUsuario as idPropietario,u.nombre as propietarioUser".
				" FROM ". $this->table ." as p INNER JOIN usuario as u ON p.idUsuario=u.id LEFT JOIN multimedia as m ON m.id=p.idImagenDestacada ORDER BY p.fechaCreacion DESC LIMIT ".$offset.",".$limit);
				$stm->execute();
			}else{
				$stm = $this->db->prepare("SELECT p.id,p.nombre,p.descripcion,p.fechaCreacion,p.fechaFinalizacion,m.url as imagenDestacada,p.localizacion,p.coordenadas,p.web,p.idUsuario as idPropietario,u.nombre as propietarioUser".
				" FROM ". $this->table ." as p INNER JOIN usuario as u ON p.idUsuario=u.id LEFT JOIN multimedia as m ON m.id=p.idImagenDestacada  WHERE u.id=? ORDER BY p.fechaCreacion DESC LIMIT ".$offset.",".$limit);
				$stm->execute(array($id));
			}
			
			$this->response->setResponse(true);
			
			while ($fila =$stm->fetch(\PDO::FETCH_ASSOC)){
				$proyecto = new \Proyecto();
				$proyecto->set($fila);
				
				//Recojo buenaIdea
				$stmSub = $this->db->prepare("SELECT idUsuario FROM buenaIdea WHERE idProyecto= ?");
				$stmSub->execute(array($proyecto->id));
				$proyecto->buenaIdea=$stmSub->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo misAreasInteres
				$stmSub = $this->db->prepare("SELECT a.id,a.nombre,a.descripcion,m.url as urlImg FROM areaProyecto as u".
				" INNER JOIN area as a ON u.idArea=a.id LEFT JOIN multimedia as m ON a.idImagenDestacada=m.id WHERE u.idProyecto= ?");
				$stmSub->execute(array($proyecto->id));
				$proyecto->misAreas=array();
				while ($filaA =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misAreas,$filaA);
				
				//Recojo vacantesProyecto
				$stmV = $this->db->prepare("SELECT id FROM vacanteProyecto WHERE idProyecto= ?");
				$stmV->execute(array($proyecto->id));
				$proyecto->vacantesProyecto=array();
				$cont=0;
				while ($filaV =$stmV->fetch(\PDO::FETCH_ASSOC)){
					$stmSub = $this->db->prepare("SELECT e.id,e.nombre,e.descripcion,v.experienciaNecesaria FROM vacanteProyecto as vp".
					" INNER JOIN vacante as v ON v.idVacante=vp.id INNER JOIN especialidad as e ON v.idEspecialidad=e.id WHERE vp.id= ?");
					$stmSub->execute(array($filaV["id"]));
					// array(array()) resultado vacantesProyecto:[{..},{..},..]
					array_push($proyecto->vacantesProyecto,array("id"=>$filaV["id"],"especialidades"=>array()));
					while($filaE =$stmSub->fetch(\PDO::FETCH_ASSOC))
						array_push($proyecto->vacantesProyecto[$cont]["especialidades"],$filaE);
					$cont++;
				}
				
				//Recojo misArchivos TODO COMPROBAR QUE EL OFFSET Y EL LIMIT SON CORRECTOS!!!
				$stmSub = $this->db->prepare("SELECT id,nombre,url,urlPreview,tipo,urlSubtitulos FROM multimedia as m WHERE m.idProyecto= ? LIMIT ".$offset.",".$limit);
				$stmSub->execute(array($proyecto->id));
				// array(array()) resultado misArchivos:[{..},{..},..]
				$proyecto->misArchivos=array();
				while ($filaAR =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misArchivos,$filaAR);
				
				//Recojo misRedesSociales
				$stmSub = $this->db->prepare("SELECT r.id,r.nombre,r.url FROM redSocial as r WHERE r.idProyecto= ?");
				$stmSub->execute(array($proyecto->id));
				// array(array()) resultado misRedesSociales:[{..},{..},..]
				$proyecto->misRedesSociales=array();
				while ($filaR =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misRedesSociales,$filaR);
				
				//Recojo misHashtag
				$stmSub = $this->db->prepare("SELECT h.nombre FROM proyectoHashtag as p INNER JOIN hashtag as h ON p.idHashtag=h.id WHERE p.idProyecto= ?");
				$stmSub->execute(array($proyecto->id));
				// array(array()) resultado misHashtag:[{..},{..},..]
				$proyecto->misHashtag=array();
				while ($filaS =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misHashtag,$filaS);

				//Recojo misAvances
				$stmSub = $this->db->prepare("SELECT a.id,a.fecha,a.nombre,a.descripcion,a.idUsuario,u.nombre as nombreUsuario, m.url as imagenDestacada".
				" FROM avance as a INNER JOIN usuario as u ON a.idUsuario=u.id LEFT JOIN multimedia as m ON m.id=a.idImagenDestacada  WHERE a.idProyecto= ?");
				$stmSub->execute(array($proyecto->id));
				// array(array()) resultado misAvances:[{..},{..},..]
				$proyecto->misAvances=array();
				while ($filaAv =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misAvances,$filaAv);
				
				//Recojo misIntegrantes
				$stmSub = $this->db->prepare("SELECT idUsuario FROM usuarioColaboradorProyecto WHERE idProyecto= ?");
				$stmSub->execute(array($proyecto->id));
				$proyecto->integrantes=$stmSub->fetchAll(\PDO::FETCH_COLUMN, 0);
	
				//Recojo solicitudes
				$stmSub = $this->db->prepare("SELECT s.id,s.fecha,s.descripcion,s.idUsuarioSolicitante,s.idProyecto,s.idVacante,p.nombre as proyecto".
				" FROM solicitudUnion as s LEFT JOIN proyecto as p ON s.idProyecto=p.id WHERE idProyecto= ?");
				$stmSub->execute(array($proyecto->id));
				// array(array()) resultado solicitudes:[{..},{..},..]
				$proyecto->solicitudes=array();
				while ($filaU =$stmSub->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->solicitudes,$filaU);

				
				array_push($proyectos["proyectos"],$proyecto);
			}
			
            $this->response->result = $proyectos;
			
            
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
			$proyectos=array("proyectos"=>array(),"totalserver"=>$totalserver["totalserver"]);
			
			for($i=0;$i<count($ids);++$i){
				$proyecto =$this->Get($ids[$i]);
				array_push($proyectos["proyectos"],$proyecto["proyecto"]);
			}
			
			$this->response->result = $proyectos;
			
            
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

			$stm = $this->db->prepare("SELECT p.id,p.nombre,p.descripcion,p.fechaCreacion,p.fechaFinalizacion,m.url as imagenDestacada,p.localizacion,p.coordenadas,p.web,p.idUsuario,u.nombre as propietarioUser".
			" FROM ". $this->table ." as p INNER JOIN usuario as u ON p.idUsuario=u.id LEFT JOIN multimedia as m ON m.id=p.idImagenDestacada WHERE p.id= ?");
			$stm->execute(array($id));
			
			$this->response->setResponse(true);
			
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);
				$proyecto = new \Proyecto();
				$proyecto->set($fila);

				//Recojo buenaIdea
				$stm = $this->db->prepare("SELECT idUsuario FROM buenaIdea WHERE idProyecto= ?");
				$stm->execute(array($proyecto->id));
				$proyecto->buenaIdea=$stm->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo misAreas
				$stm = $this->db->prepare("SELECT a.id,a.nombre,a.descripcion,m.url FROM areaProyecto as u".
				" INNER JOIN area as a ON u.idArea=a.id LEFT JOIN multimedia as m ON a.idImagenDestacada=m.id WHERE u.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				$proyecto->misAreas=array();
				while ($filaA =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misAreas,$filaA);
				
				//Recojo vacantesProyecto
				$stmV = $this->db->prepare("SELECT id FROM vacanteProyecto WHERE idProyecto= ?");
				$stmV->execute(array($proyecto->id));
				$proyecto->vacantesProyecto=array();
				$cont=0;
				while ($filaV =$stmV->fetch(\PDO::FETCH_ASSOC)){
					$stmSub = $this->db->prepare("SELECT e.id,e.nombre,e.descripcion,v.experienciaNecesaria FROM vacanteProyecto as vp".
					" INNER JOIN vacante as v ON v.idVacante=vp.id INNER JOIN especialidad as e ON v.idEspecialidad=e.id WHERE vp.id= ?");
					$stmSub->execute(array($filaV["id"]));
					// array(array()) resultado vacantesProyecto:[{..},{..},..]
					array_push($proyecto->vacantesProyecto,array("id"=>$filaV["id"],"especialidades"=>array()));
					while($filaE =$stmSub->fetch(\PDO::FETCH_ASSOC))
						array_push($proyecto->vacantesProyecto[$cont]["especialidades"],$filaE);
					$cont++;
				}
				
				//Recojo misArchivos TODO COMPROBAR QUE EL OFFSET Y EL LIMIT SON CORRECTOS!!!
				$stm = $this->db->prepare("SELECT id,nombre,url,urlPreview,tipo,urlSubtitulos FROM multimedia as m WHERE m.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misArchivos:[{..},{..},..]
				$proyecto->misArchivos=array();
				while ($filaAR =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misArchivos,$filaAR);
				
				//Recojo misRedesSociales
				$stm = $this->db->prepare("SELECT r.id,r.nombre,r.url FROM redSocial as r WHERE r.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misRedesSociales:[{..},{..},..]
				$proyecto->misRedesSociales=array();
				while ($filaR =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misRedesSociales,$filaR);
				
				//Recojo misHashtag
				$stm = $this->db->prepare("SELECT h.nombre FROM proyectoHashtag as p INNER JOIN hashtag as h ON p.idHashtag=h.id WHERE p.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misHashtag:[{..},{..},..]
				$proyecto->misHashtag=array();
				while ($filaS =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misHashtag,$filaS);

				//Recojo misAvances
				$stm = $this->db->prepare("SELECT a.id,a.fecha,a.nombre,a.descripcion,a.idUsuario,u.nombre as nombreUsuario, m.url as imagenDestacada".
				" FROM avance as a INNER JOIN usuario as u ON a.idUsuario=u.id LEFT JOIN multimedia as m ON m.id=a.idImagenDestacada  WHERE a.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misAvances:[{..},{..},..]
				$proyecto->misAvances=array();
				while ($filaAv =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misAvances,$filaAv);
				
				//Recojo misIntegrantes
				$stm = $this->db->prepare("SELECT idUsuario FROM usuarioColaboradorProyecto WHERE idProyecto= ?");
				$stm->execute(array($proyecto->id));
				$proyecto->integrantes=$stm->fetchAll(\PDO::FETCH_COLUMN, 0);
				
				//Recojo solicitudes
				$stm = $this->db->prepare("SELECT s.id,s.fecha,s.descripcion,s.idUsuarioSolicitante,s.idProyecto,s.idVacante,p.nombre as proyecto".
				" FROM solicitudUnion as s LEFT JOIN proyecto as p ON s.idProyecto=p.id WHERE idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado solicitudes:[{..},{..},..]
				$proyecto->solicitudes=array();
				while ($filaU =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->solicitudes,$filaU);

				
			
            $this->response->result = array("proyecto"=>$proyecto);
			
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}
    }
	
	
	
	public function GetMultimedia($offset,$limit,$id)
    {
		try
		{	
			//Recojo el total de multimedia del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM multimedia");
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
            $multimedia=array("multimedia"=>array(),"totalserver"=>$totalserver);
			
			$this->response->setResponse(true);
			
			$stm = $this->db->prepare("SELECT id,nombre,url,urlPreview,tipo,urlSubtitulos FROM multimedia as m WHERE m.idProyecto= ? LIMIT ".$offset.",".$limit);
			$stm->execute(array($id));
			// array(array()) resultado misArchivos:[{..},{..},..]
			while ($filaAR =$stm->fetch(\PDO::FETCH_ASSOC))
				array_push($multimedia["multimedia"],$filaAR);
				
            $this->response->result = $multimedia;
			
            
            return $this->response->result;
		}
		catch(Exception $e)
		{
			$this->response->setResponse(false, $e->getMessage());
            return $this->response;
		}
    }
	public function InsertColaborador($data)
	{
		
		try 
		{
                $sql = "INSERT INTO usuarioColaboradorProyecto
                            (id, idUsuario, idProyecto,idEspecialidad)
                            VALUES (NULL,?,?,?)";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idProyecto'],
							$data['idEspecialidad']
                        )
                    ); 
					
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	public function InsertAvance($data)
	{
		
		try 
		{
                $sql = "INSERT INTO avance
                            (id, idUsuario, idProyecto,nombre,descripcion,idImagenDestacada,fecha)
                            VALUES (NULL,?,?,?,?,?,?)";
							
				$stm = $this->db->prepare("SELECT url FROM multimedia WHERE id= ?");
				$stm->execute(array($data['idImagenDestacada']));
				
				$fila =$stm->fetch(\PDO::FETCH_ASSOC);
				
                $avance=array(
                            "idUsuario"=>$data['idUsuario'],
                            "idProyecto"=>$data['idProyecto'],
							"nombre"=>$data['nombre'],
							"descripcion"=>$data['descripcion'],
							"imagenDestacada"=>$fila['url'],
							"fecha"=>date("Y-m-d H:i:s")
                        );
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['idUsuario'],
                            $data['idProyecto'],
							$data['nombre'],
							$data['descripcion'],
							$data['idImagenDestacada'],
							date("Y-m-d H:i:s")
                        )
                    ); 
					
			$avance["id"]=$this->db->lastInsertId();
			
			$this->response->setResponse(true);
			$this->response->result=$avance;
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
	public function InsertMultimedia($data)
	{
		
		try 
		{
                $sql = "INSERT INTO multimedia
                            (id, nombre, tipo,url)
                            VALUES (NULL,?,?,?)";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
                            $data['nombre'],
                            "imagen",
							$data['url']
                        )
                    ); 
					
			$this->response->setResponse(true);
			$id =$this->db->lastInsertId();
			$this->response->result=array("id" =>$id );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
		}
    }
    public function DeleteVacante($id)
    {
		try 
		{
			$stm = $this->db->prepare("DELETE FROM vacante WHERE idVacante = ?");			          
			$stm->execute(array($id));
			
			$stm = $this->db->prepare("SELECT count(*) as filas FROM vacante WHERE idVacante = ?");			          
			$stm->execute(array($id));
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);
			
			//Sino hay más vacantes con el mismo id
			if($fila["filas"]==0){
				$stm = $this->db->prepare("DELETE FROM vacanteProyecto WHERE id = ?");			          
				$stm->execute(array($id));
			}
			
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
            return $this->response->result;
		} catch (Exception $e) 
		{
			$this->response->setResponse(false, $e->getMessage());
		}
    }
	
	public function OcuparVacante($data)
    {
		try 
		{
			$stm = $this->db->prepare("DELETE FROM vacante WHERE idVacante = ?");			          
			$stm->execute(array($data["id"]));
			
			$stm = $this->db->prepare("SELECT count(*) as filas FROM vacante WHERE idVacante = ?");			          
			$stm->execute(array($data["id"]));
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);
			
			//Sino hay más vacantes con el mismo id
			if($fila["filas"]==0){
				$stm = $this->db->prepare("DELETE FROM vacanteProyecto WHERE id = ?");			          
				$stm->execute(array($data["id"]));
			}
			
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
			$this->InsertColaborador($data);
			
			$this->DeleteSolicitudUnion($data);
			
			$this->InsertaNotificacionSolicitud($data);
			
            return $this->response->result;
		} catch (Exception $e) 
		{
			$this->response->setResponse(false, $e->getMessage());
		}
    }
	public function InsertaNotificacionSolicitud($data){
			
			//Busco el proyecto
			$stm = $this->db->prepare("SELECT nombre as proyecto FROM  proyecto WHERE id=?");
			$stm->execute(array($data["idProyecto"]));
			$proyecto = $stm->fetch(\PDO::FETCH_ASSOC);
			
			//Busco el usuario
			$stm = $this->db->prepare("SELECT user as usuario FROM  usuario WHERE id=?");
			$stm->execute(array($data["idUsuario"]));
			$usuario = $stm->fetch(\PDO::FETCH_ASSOC);
			
			$datos=array("nombre"=>"Nuevo colaborador en ".$proyecto["proyecto"]."!",
			"descripcion"=>"El usuario ".$usuario["usuario"]." ahora es colaborador en ".$proyecto["proyecto"],
			 "idUsuario"=>$data['idUsuario'],
			 "idProyecto"=>$data['idProyecto'],
			 "idVacante"=>$data['idVacante'],
			 "tipo"=>"ocupar",
			 "accion"=>"insert");
			 
			$pub = new NotificacionModel();
			$pub->InsertNotificacion($datos);
	}
	public function DeleteSolicitudUnion($data)
	{
		
		try 
		{
                $sql = "DELETE FROM solicitudUnion WHERE idUsuarioSolicitante=? AND idProyecto=? AND idVacante=?";
                
                $this->db->prepare($sql)
                     ->execute(
                        array(
							$data['idUsuario'],
                            $data['idProyecto'],
                            $data['id']
                        )
                    ); 
            
			$this->response->setResponse(true);
			$this->response->result=array("resultado" =>"ok" );
			
            return $this->response->result;
		}catch (Exception $e) 
		{
            $this->response->setResponse(false, $e->getMessage());
			 return $this->response;
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