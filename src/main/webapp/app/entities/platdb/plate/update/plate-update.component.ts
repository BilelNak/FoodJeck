import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PlateFormService, PlateFormGroup } from './plate-form.service';
import { IPlate } from '../plate.model';
import { PlateService } from '../service/plate.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-plate-update',
  templateUrl: './plate-update.component.html',
})
export class PlateUpdateComponent implements OnInit {
  isSaving = false;
  plate: IPlate | null = null;

  editForm: PlateFormGroup = this.plateFormService.createPlateFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected plateService: PlateService,
    protected plateFormService: PlateFormService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plate }) => {
      this.plate = plate;
      if (plate) {
        this.updateForm(plate);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewaydbApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plate = this.plateFormService.getPlate(this.editForm);
    if (plate.id !== null) {
      this.subscribeToSaveResponse(this.plateService.update(plate));
    } else {
      this.subscribeToSaveResponse(this.plateService.create(plate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlate>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(plate: IPlate): void {
    this.plate = plate;
    this.plateFormService.resetForm(this.editForm, plate);
  }
}
