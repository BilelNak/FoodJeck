import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlateService } from '../service/plate.service';

import { PlateComponent } from './plate.component';

describe('Plate Management Component', () => {
  let comp: PlateComponent;
  let fixture: ComponentFixture<PlateComponent>;
  let service: PlateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'plate', component: PlateComponent }]), HttpClientTestingModule],
      declarations: [PlateComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(PlateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlateComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PlateService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.plates?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to plateService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getPlateIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPlateIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
