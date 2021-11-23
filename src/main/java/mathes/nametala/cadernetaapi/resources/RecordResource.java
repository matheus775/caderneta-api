package mathes.nametala.cadernetaapi.resources;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mathes.nametala.cadernetaapi.event.NewResourceEvent;
import mathes.nametala.cadernetaapi.model.entitys.RecordEntity;
import mathes.nametala.cadernetaapi.repository.filter.RecordFilter;
import mathes.nametala.cadernetaapi.services.RecordService;

@RestController
@RequestMapping("/records")
public class RecordResource {

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private RecordService recordService;

	@GetMapping
	@PreAuthorize("anyAuthority()")
	public Page<RecordEntity> getRecords(Pageable pageable,RecordFilter recordFilter) {
		return recordService.getRecords(pageable, recordFilter);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public RecordEntity getRecord(@PathVariable Long id) {
		return recordService.getRecord(id);
	}
	
	@PostMapping
	@PreAuthorize("anyAuthority()")
	public ResponseEntity<RecordEntity> newRecord(@Valid @RequestBody RecordEntity record, HttpServletResponse response) {
		
		RecordEntity newRecord = recordService.newRecord(record);
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, newRecord.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newRecord);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delRecord(@PathVariable Long id) {
		recordService.delRecord(id);
	};
	
	@PutMapping("/{id}")
	@PreAuthorize("anyAuthority()")
	public ResponseEntity<RecordEntity> uptdRecord(@PathVariable Long id, @Valid @RequestBody RecordEntity record, HttpServletResponse response) {
		RecordEntity changedRecord = recordService.uptdRecord(record,id);
		applicationEventPublisher.publishEvent(new NewResourceEvent(this, response, changedRecord.getId()));
		
		return ResponseEntity.status(HttpStatus.OK).body(changedRecord);
	}
}
