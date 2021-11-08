<?php

$title = $_POST['title'];
$note = $_POST['text'];
$host = "localhost";
$db = "notes";
$user="root";
$password="";

$conn = mysqli_connect($host,$user,$password,$db);

    // public function getInstance(){
    //     try{
    //         $this->conn= new PDO("mysql:host=".$this->host.";dbname=".$this->db, $this->user,$this->password);
    //     }catch (PDOException $exc){
    //         echo "failed to connect".$exc->getMessage();
    //     }
    //     return $this->conn;
    // }
   
// $sql =" INSERT INTO notes values('".$title.', '".$note.');";

// if(mysqli_query($conn,$sql)){
//         echo "Data insertion succecss...";
//     }
// else{
//         echo "Error while insertion";
//     }
mysqli_close($conn)
// }


?>