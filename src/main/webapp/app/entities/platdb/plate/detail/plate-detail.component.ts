import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlate } from '../plate.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-plate-detail',
  templateUrl: './plate-detail.component.html',
})
export class PlateDetailComponent implements OnInit {
  plate: IPlate | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plate }) => {
      this.plate = plate;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
