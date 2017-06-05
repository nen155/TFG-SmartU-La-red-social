<?php
namespace App\Model;

use App\Lib\Database;
use App\Lib\Response;
include_once "class.proyecto.php";

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
    
    public function GetAll($offset,$limit)
    {
		try
		{	
			//Recojo el total de usuarios del server
			$stm = $this->db->prepare("SELECT count(1) as totalserver FROM ". $this->table);
			$stm->execute();
			$totalserver = $stm->fetch(\PDO::FETCH_ASSOC);
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
            $proyectos=array("proyectos"=>array("proyectos"=>array()),"totalserver"=>$totalserver["totalserver"]);
			
			$result = array();
			$stm = $this->db->prepare("SELECT p.id,p.nombre,p.descripcion,p.fechaCreacion,p.fechaFinalizacion,m.url as imagenDestacada,p.localizacion,p.coordenadas,p.web,p.idUsuario as idPropietario,u.nombre as propietarioUser".
			" FROM ". $this->table ." as p INNER JOIN usuario as u ON p.idUsuario=u.id LEFT JOIN multimedia as m ON m.id=p.idImagenDestacada LIMIT ".$offset.",".$limit);
			$stm->execute();
			
			$this->response->setResponse(true);
			
			while ($fila =$stm->fetch(\PDO::FETCH_ASSOC)){
				$proyecto = new Proyecto();
				$proyecto->set($fila);
				//Recojo los campos del proyecto
				/*$proyecto->id=$fila["id"] ;
				$proyecto->nombre=$fila["nombre"] ;
				$proyecto->apellidos=$fila["apellidos"] ;
				$proyecto->verificado=$fila["verificado"] ;
				$proyecto->user=$fila["user"] ;
				$proyecto->email=$fila["email"] ;
				$proyecto->nPuntos=$fila["nPuntos"] ;
				$proyecto->biografia=$fila["biografia"] ;
				$proyecto->web=$fila["web"] ;
				$proyecto->imagenPerfil=$fila["imagenPerfil"] ;
				$proyecto->uid=$fila["uid"] ;
				$proyecto->firebaseToken=$fila["firebaseToken"] ;*/
				
				//Recojo buenaIdea
				$stm = $this->db->prepare("SELECT idUsuario FROM buenaIdea WHERE idProyecto= ?");
				$stm->execute(array($proyecto->id));
				$proyecto->buenaIdea=$stm->fetchAll();
				
				//Recojo misAreasInteres
				$stm = $this->db->prepare("SELECT a.id,a.nombre,a.descripcion,m.url as urlImg FROM areaProyecto as u".
				" INNER JOIN area as a ON u.idArea=a.id LEFT JOIN multimedia as m ON a.idImagenDestacada=m.id WHERE u.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				while ($filaA =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misAreas,$filaA);
				
				//Recojo vacantesProyecto
				$stm = $this->db->prepare("SELECT e.id,e.nombre,e.descripcion,v.experienciaNecesaria FROM vacanteProyecto as vp".
				" INNER JOIN vacante as v ON v.idVacante=vp.id INNER JOIN especialidad as e ON v.idEspecialidad=e.id WHERE vp.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado vacantesProyecto:[{..},{..},..]
				while ($filaE =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->vacantesProyecto,$filaE);
				
				//Recojo misArchivos TODO COMPROBAR QUE EL OFFSET Y EL LIMIT SON CORRECTOS!!!
				$stm = $this->db->prepare("SELECT id,nombre,url,urlPreview,tipo,urlSubtitulos FROM multimedia as m WHERE m.idProyecto= ? LIMIT ".$offset.",".$limit);
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misArchivos:[{..},{..},..]
				while ($filaAR =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misArchivos,$filaAR);
				
				//Recojo misRedesSociales
				$stm = $this->db->prepare("SELECT r.id,r.nombre,r.url FROM redSocial as r WHERE r.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misRedesSociales:[{..},{..},..]
				while ($filaR =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misRedesSociales,$filaR);
				
				//Recojo misHashtag
				$stm = $this->db->prepare("SELECT h.nombre FROM proyectoHashtag as p INNER JOIN hashtag as h ON p.idHashtag=h.id WHERE p.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misHashtag:[{..},{..},..]
				while ($filaS =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misHashtag,$filaS);

				//Recojo misAvances
				$stm = $this->db->prepare("SELECT a.id,a.fecha,a.nombre,a.descripcion,a.idUsuario,u.nombre as nombreUsuario, m.url as imagenDestacada".
				" FROM avance as a INNER JOIN usuario as u ON a.idUsuario=u.id LEFT JOIN multimedia as m ON m.id=a.idImagenDestacada  WHERE a.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misAvances:[{..},{..},..]
				while ($filaAv =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misAvances,$filaAv);
				
				//Recojo misIntegrantes
				$stm = $this->db->prepare("SELECT idUsuario FROM usuarioColaboradorProyecto WHERE idProyecto= ?");
				$stm->execute(array($proyecto->id));
				$proyecto->integrantes=$stm->fetchAll();

				
				array_push($proyectos["proyectos"]["proyectos"],$proyecto);
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

			$result = array();
			$stm = $this->db->prepare("SELECT p.id,p.nombre,p.descripcion,p.fechaCreacion,p.fechaFinalizacion,m.url as imagenDestacada,p.localizacion,p.coordenadas,p.web,p.idPropietario,u.nombre as propietarioUser".
			" FROM ". $this->table ." as p INNER JOIN usuario as u ON p.idPropietario=u.id LEFT JOIN multimedia as m ON m.id=p.idImagenDestacada WHERE p.id= ?");
			$stm->execute(array($id));
			
			$this->response->setResponse(true);
			
			$fila =$stm->fetch(\PDO::FETCH_ASSOC);
				$proyecto = new Proyecto();
				$proyecto->set($fila);

				//Recojo buenaIdea
				$stm = $this->db->prepare("SELECT idUsuario FROM buenaIdea WHERE idProyecto= ?");
				$stm->execute(array($proyecto->id));
				$proyecto->buenaIdea=$stm->fetchAll();
				
				//Recojo misAreasInteres
				$stm = $this->db->prepare("SELECT a.id,a.nombre,a.descripcion,m.url FROM areaProyecto as u".
				" INNER JOIN area as a ON u.idArea=a.id LEFT JOIN multimedia as m ON a.idImagenDestacada=m.id WHERE u.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				while ($filaA =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misAreas,$filaA);
				
				//Recojo vacantesProyecto
				$stm = $this->db->prepare("SELECT e.id,e.nombre,e.descripcion,v.experienciaNecesaria FROM vacanteProyecto as vp".
				" INNER JOIN vacante as v ON v.idVacante=vp.id INNER JOIN especialidad as e ON v.idEspecialidad=e.id WHERE vp.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado vacantesProyecto:[{..},{..},..]
				while ($filaE =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->vacantesProyecto,$filaE);
				
				//Recojo misArchivos TODO COMPROBAR QUE EL OFFSET Y EL LIMIT SON CORRECTOS!!!
				$stm = $this->db->prepare("SELECT id,nombre,url,urlPreview,tipo,urlSubtitulos FROM multimedia as m WHERE m.idProyecto= ? LIMIT ".$offset.",".$limit);
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misArchivos:[{..},{..},..]
				while ($filaAR =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misArchivos,$filaAR);
				
				//Recojo misRedesSociales
				$stm = $this->db->prepare("SELECT r.id,r.nombre,r.url FROM redSocial as r WHERE r.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misRedesSociales:[{..},{..},..]
				while ($filaR =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misRedesSociales,$filaR);
				
				//Recojo misHashtag
				$stm = $this->db->prepare("SELECT h.nombre FROM proyectoHashtag as p INNER JOIN hashtag as h ON p.idHashtag=h.id WHERE p.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misHashtag:[{..},{..},..]
				while ($filaS =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misHashtag,$filaS);

				//Recojo misAvances
				$stm = $this->db->prepare("SELECT a.id,a.fecha,a.nombre,a.descripcion,a.idUsuario,u.nombre as nombreUsuario, m.url as imagenDestacada".
				" FROM avance as a INNER JOIN usuario as u ON a.idUsuario=u.id LEFT JOIN multimedia as m ON m.id=a.idImagenDestacada  WHERE a.idProyecto= ?");
				$stm->execute(array($proyecto->id));
				// array(array()) resultado misAvances:[{..},{..},..]
				while ($filaAv =$stm->fetch(\PDO::FETCH_ASSOC))
					array_push($proyecto->misAvances,$filaAv);
				
				//Recojo misIntegrantes
				$stm = $this->db->prepare("SELECT idUsuario FROM usuarioColaboradorProyecto WHERE idProyecto= ?");
				$stm->execute(array($proyecto->id));
				$proyecto->integrantes=$stm->fetchAll();

				
			
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
			$totalserver = $stm->fetch()["totalserver"];
			//Utilizo este modelo porque es el que tengo la APP de Android, podría simplificarse
            $multimedia=array("multimedia"=>array("multimedia"=>array()),"totalserver"=>$totalserver);
			
			$this->response->setResponse(true);
			
			$stm = $this->db->prepare("SELECT id,nombre,url,urlPreview,tipo,urlSubtitulos FROM multimedia as m WHERE m.idProyecto= ? LIMIT ".$offset.",".$limit);
			$stm->execute(array($id));
			// array(array()) resultado misArchivos:[{..},{..},..]
			while ($filaAR =$stm->fetch(\PDO::FETCH_ASSOC))
				array_push($multimedia["multimedia"]["multimedia"],$filaAR);
				
            $this->response->result = $multimedia;
			
            
            return $this->response->result;
		}
		catch(Exception $e)
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