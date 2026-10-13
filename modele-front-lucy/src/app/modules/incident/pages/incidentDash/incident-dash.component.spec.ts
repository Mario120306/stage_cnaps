import { ComponentFixture, TestBed } from '@angular/core/testing';
import { IncidentDashComponent } from './incident-dash.component';

describe('IncidentDashComponent', () => {
  let component: IncidentDashComponent;
  let fixture: ComponentFixture<IncidentDashComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IncidentDashComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncidentDashComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should filter incidents', () => {
    component.incidents = [
      { id: 'INC-001', demandeur: { matricule: 'EMP001', nom: 'Test' }, servicesAffecte: [], description: 'Test', type: { id: 'TYPE', libelle: 'Test' }, dateCreation: '2023-01-01' }
    ];
    component.searchTerm = 'INC-001';
    component.filterIncidents();
    expect(component.filteredIncidents.length).toBe(1);
  });

  // Ajoutez d'autres tests ici
});
