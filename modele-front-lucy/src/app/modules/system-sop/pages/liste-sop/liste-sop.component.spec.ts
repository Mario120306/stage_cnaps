import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ListeSopComponent } from "./liste-sop.component"

describe('ListeSopComponent',()=>{
    let component:ListeSopComponent;
    let fixture:ComponentFixture<ListeSopComponent>;

    beforeEach(async()=>{
        await TestBed.configureTestingModule({
            declarations:[ListeSopComponent]
        })
        .compileComponents()
    });
    BeforeEach(()=>{
        fixture=TestBed.createComponent(ListeSopComponent);
        component=fixture.componentInstance;
        fixture.detectChanges();
    });
    it('should create',()=>{
        expect(component).toBeTruthy();
    });
});