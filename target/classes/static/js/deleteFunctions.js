function deleting(){
	swal({
	  title: "Are you sure?",
	  text: "Once deleted, you will not be able to recover this imaginary book!",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((OK) => {
	  if (OK) {
		$.ajax({
		url:"/",
		success: function(res) {
			console.log(res);
		},
		});
	    swal("Poof! Your imaginary book has been deleted!", {
	      icon: "success",
	    }).then((ok)=>{
			if(ok){
				location.href="/";
			}
		})
	  } else {
	    swal("Your imaginary book is safe!");
	  }
	});
}