import { ComponentFixture, TestBed } from "@angular/core/testing";
import { VueSopComponent } from "./vue-sop.component";

describe('VueSopComponent',()=>{
    let component:VueSopComponent;
    let fixture:ComponentFixture<VueSopComponent>

    beforeEach(async()=>{
        await TestBed.configureTestingModule({
            declarations:[VueSopComponent]
        })
        .compileComponents()
    });
    BeforeEach(()=>{
        fixture=TestBed.createComponent(VueSopComponent);
        component=fixture.componentInstance;
        fixture.detectChanges();
    });
    it('should create',()=>{
        expect(component).toBeTruthy();
    });
});