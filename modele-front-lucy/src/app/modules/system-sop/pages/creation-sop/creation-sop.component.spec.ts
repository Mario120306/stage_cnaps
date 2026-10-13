import { ComponentFixture, TestBed } from "@angular/core/testing";
import { CreationSopComponent } from "./creation-sop.component";

describe('CreationSopComponent',()=>{
    let component:CreationSopComponent;
    let fixture:ComponentFixture<CreationSopComponent>

    beforeEach(async()=>{
        await TestBed.configureTestingModule({
            declarations:[CreationSopComponent]
        })
        .compileComponents()
    });
    BeforeEach(()=>{
        fixture=TestBed.createComponent(CreationSopComponent);
        component=fixture.componentInstance;
        fixture.detectChanges();
    });
    it('should create',()=>{
        expect(component).toBeTruthy();
    });
});