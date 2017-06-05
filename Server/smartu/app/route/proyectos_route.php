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
                $um->GetAll($data["offset"],$data["limit"])
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
	
});