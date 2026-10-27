import { Component, OnInit } from "@angular/core";
import { ToastrService } from "ngx-toastr";
import {Service as ServiceModel} from '../../model/service.model';
import {Direction as DirectionModel} from '../../model/direction.model';
import {Sop as SopModel} from '../../model/sop.model';
import {ServiceService}  from '../../services/service.service';
import {DirectionService} from '../../services/direction.service';
import {SopService} from '../../services/sop.service';

@Component({
    selector:'app-listeSOP',
    templateUrl:'./liste-sop.component.html',
    styleUrls: ['./liste-sop.component.scss'],
})
export class ListeSopComponent implements OnInit{

    services:ServiceModel[]=[];
    directions:DirectionModel[]=[];
    sops:SopModel[]=[];

    
    page = 0;
    size =5;
    totalElements = 0;
    totalPages = 0;


    filterTitre = '';
    filterDirection = '#';
    filterService = '#';
    filterAuteur = '';
    filterDateFrom: string | null = null;
    filterDateTo: string | null = null;

    constructor(private toastr:ToastrService,
                private serviceService:ServiceService,
                private directionService:DirectionService,
                private sopService:SopService){}
    async ngOnInit(): Promise<void> {
    
        try {
            this.services = await this.serviceService.getAll();
            this.directions = await this.directionService.getAll();
            await this.loadPage(0);
        } catch (error) {
            console.error('Erreur lors du chargement des services', error);
        }
    }

    async loadPage(page:number){
        this.page = page;
        const params: Record<string, string | number | boolean> = { page: this.page, size: this.size };
        if (this.filterTitre) params['titre'] = this.filterTitre;
        if (this.filterDirection && this.filterDirection !== '#') params['direction'] = this.filterDirection;
        if (this.filterService && this.filterService !== '#') params['service'] = this.filterService;
        if (this.filterAuteur) params['auteur'] = this.filterAuteur;
        if (this.filterDateFrom) params['dateFrom'] = this.filterDateFrom;
        if (this.filterDateTo) params['dateTo'] = this.filterDateTo;

        try{

            const response = await this.sopService.getAll(params);
            this.sops = response.content ?? [];
            this.totalElements = response.totalElements ?? 0;
            this.totalPages = response.totalPages ?? 0;
        }catch(err){
            console.error('Erreur chargement sops', err);
        }
    }

    async search(){
        await this.loadPage(0);
    }

    async resetFilters(){
        this.filterTitre = '';
        this.filterDirection = '#';
        this.filterService = '#';
        this.filterAuteur = '';
        this.filterDateFrom = null;
        this.filterDateTo = null;
        await this.loadPage(0);
    }

    async prev(){
        if (this.page > 0) await this.loadPage(this.page - 1);
    }

    async next(){
        if (this.page + 1 < this.totalPages) await this.loadPage(this.page + 1);
    }

    async goToPage(page: number){
        if (page >= 0 && page < this.totalPages && page !== this.page) {
            await this.loadPage(page);
        }
    }

    get pageNumbers(): number[] {
        const firstPage = Math.floor(this.page / 5) * 5;
        const visiblePageCount = Math.min(5, this.totalPages - firstPage);
        return Array.from({ length: visiblePageCount }, (_, index) => firstPage + index);
    }

    info(): void {
        this.toastr.info('Message / notification INFO', '');
    }

    success(): void {
        this.toastr.success('Message / notification SUCCESS', '');
    }

    warning(): void {
        this.toastr.warning('Message / notification WARNING', '');
    }

    error(): void {
        this.toastr.error('Message / notification ERROR', '');
    }
}