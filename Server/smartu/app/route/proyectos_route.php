<?php
use App\Model\ProyectoModel;

$app->group('/project/', function () {
    
    $this->get('test', function ($req, $res, $args) {
        return $res->getBody()
                   ->write('Hello Users');
    });
    
    $this->post('proyectos', function ($req, $res, $args) {
        $um = new ProyectoModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->GetAll($data["offset"],$data["limit"],$data["idUsuario"])
            )
        );
    });
    
    $this->post('multimedia', function ($req, $res, $args) {
        $um = new ProyectoModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->GetMultimedia($data["offset"],$data["limit"],$data["idProyecto"])
            )
        );
    });
	
	$this->post('eliminavacante', function ($req, $res, $args) {
        $um = new ProyectoModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->DeleteVacante($data["id"])
            )
        );
    });
	
	$this->post('guardarcolaborador', function ($req, $res, $args) {
        $um = new ProyectoModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertColaborador($data)
            )
        );
    });
	
	$this->post('ocuparvacante', function ($req, $res, $args) {
        $um = new ProyectoModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->OcuparVacante($data)
            )
        );
    });
	
	$this->post('imagenavance', function ($req, $res, $args){
		$uploadDir = 'imagenes/';
		$error = false;
		
		if (!empty($_FILES['fileUpload']) && $_FILES['fileUpload']['error'] !== 4) {
			if (!$_FILES['fileUpload']['error']) {
				if (!move_uploaded_file($_FILES['fileUpload']['tmp_name'], $uploadDir.$_FILES['fileUpload']['name'])) {
					echo 'There is a error while processing uploaded file';
				}
			} else {
				echo 'Error while uploading file';
			}
		}
		$datos = array();
		$datos["url"]= $_FILES['fileUpload']['name'];
		$datos["nombre"]= preg_replace('/(.+)\.\w+$/U', $_FILES['fileUpload']['name']);
		$um = new ProyectoModel();
		return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertMultimedia($datos)
            )
        );
	});
	
	$this->post('crearavance', function ($req, $res, $args) {
        $um = new ProyectoModel();
        $data= $req->getParsedBody();
        return $res
           ->withHeader('Content-type', 'application/json')
           ->getBody()
           ->write(
            json_encode(
                $um->InsertAvance($data)
            )
        );
    });
});