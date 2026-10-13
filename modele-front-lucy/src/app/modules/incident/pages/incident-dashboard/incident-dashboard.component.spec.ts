import { ComponentFixture, TestBed } from '@angular/core/testing';
import { IncidentDashboardComponent } from './incident-dashboard.component';
import { IncidentDashboardModule } from './incident-dashboard.module';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

describe('IncidentDashboardComponent', () => {
  let component: IncidentDashboardComponent;
  let fixture: ComponentFixture<IncidentDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommonModule, FormsModule,IncidentDashboardModule],
      declarations: [IncidentDashboardComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(IncidentDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load data on init', () => {
    spyOn(component as any, 'loadData');
    component.ngOnInit();
    expect((component as any).loadData).toHaveBeenCalled();
  });

  it('should change time range', () => {
    spyOn(component as any, 'loadData');
    component.setTimeRange('30d');
    expect(component.timeRange).toBe('30d');
    expect((component as any).loadData).toHaveBeenCalled();
  });

  it('should calculate stats correctly', () => {
    component.incidents = [
      {
        id: 'INC-001',
        title: 'Test',
        severity: 'medium',
        status: 'closed',
        startTime: new Date(Date.now() - 3600000),
        endTime: new Date(),
        resolutionTime: 60
      }
    ];
    component.calculateStats();
    expect(component.stats).toBeTruthy();
    expect(component.stats?.incidentsCount).toBe(1);
  });

  it('should return correct severity colors', () => {
    expect(component.getSeverityColor('critical')).toBe('#ff3860');
    expect(component.getSeverityColor('low')).toBe('#66bb6a');
  });
});