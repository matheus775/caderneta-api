package mathes.nametala.cadernetaapi.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.services.RecordService;

@RestController
@RequestMapping("/records")
public class RecordResource {

	@Autowired
	private RecordService recordService;
	
	@GetMapping("/byUserName/{userName}")
	@PreAuthorize("anyAuthority()")
	public List<RecordEntity> getByUserName(@PathVariable String userName){
		return recordService.getByUsername(userName);
	}
	
	@GetMapping("/byCustomerName/{customerName}")
	@PreAuthorize("anyAuthority()")
	public List<RecordEntity> getByCustomerName(@PathVariable String customerName){
		return recordService.getByCustomerName(customerName);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public RecordEntity getRecord(@PathVariable Long id) {
		return recordService.getRecord(id);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public void newRecord(@RequestBody RecordEntity record) {
		recordService.newRecord(record);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public void delRecord(@PathVariable Long id) {
		recordService.delRecord(id);
	};
	
	@PutMapping("/{id}")
	public void uptdRecord(@RequestBody RecordEntity record,@PathVariable Long id) {
		recordService.uptdRecord(record, id);
	}
}
