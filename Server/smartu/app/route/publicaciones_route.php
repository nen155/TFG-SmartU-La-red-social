<?php
use App\Model\ComentarioModel;
use App\Model\ProyectoModel;
use App\Model\NotificacionModel;
use App\Model\UserModel;
use App\Model\AreaModel;

$app->group('/publish/', function () {
    
    $this->get('test', function ($req, $res, $args) {
        return $res->getBody()
                   ->write('Hello Users');
    });
    
    $this->post('publicaciones', function ($req, $res) {
        $com = new ComentarioModel();
		$pro = new ProyectoModel();
		$not = new NotificacionModel();
		$use = new UserModel();
		$are = new AreaModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
				array("publicaciones"=>array(
				"comentarios"=>$com->GetAll($data["offset"],$data["limit"]),
				"proyectos"=>$pro->GetAll($data["offset"],$data["limit"]),
				"notificaciones"=>$not->GetAll($data["offset"],$data["limit"]),
				"usuarios"=>$use->GetAll($data["offset"],$data["limit"]),
				"areas"=>$are->GetAll($data["offset"],$data["limit"])
				))
                
            )
        );
    });
    
    $this->post('publicacion', function ($req, $res) {
		$data= $req->getParsedBody();
		$tipo = $data["tipo"];
		switch($tipo){
			case 0:
			$pub = new ProyectoModel();
			break;
			case 1:
			$pub = new UserModel();
			break;
			case 2:
			$pub = new NotificacionModel();
			break;
			case 3:
			$pub = new ComentarioModel();
			break;
		}

        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
               array("publicacion"=> $pub->Get($data["idPublicacion"]))
            )
        );
    });
	
	$this->post('publicacionesById', function ($req, $res) {
		$data= $req->getParsedBody();
		$tipo = $data["tipo"];
		switch($tipo){
			case 0:
			$pub = new ProyectoModel();
			$tipo="proyectos";
			break;
			case 1:
			$pub = new UserModel();
			$tipo="usuarios";
			break;
			case 2:
			$pub = new NotificacionModel();
			$tipo="notificaciones";
			break;
			case 3:
			$pub = new ComentarioModel();
			$tipo="comentarios";
			break;
		}

        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
               array("publicaciones"=>array($tipo => $pub->GetbyIds($data["publicaciones"])))
            )
        );
    });
	
});